package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.omsorgpgodskriving

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevbakingRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.Felles
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.BrevDistribueringsInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalforingInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalforingService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.annotation.PostConstruct

data class OmsorgPGodskrivingRecord(
    val saksnummer: String,
    val letterData: String, //TODO sen dinn data og lag struktur i consumer

)

@Service
class OmsorgPGodskrivingListener(
    private val journalforingService: JournalforingService,
    @Value("OMSORGP_GODSKRIVING_TOPIC") private val topic: String,
    registry: MeterRegistry,
) {
    private val counterStartingListener = registry.counter("init", "topic", topic)
    private val counterFailedRecords = registry.counter("omsorgp_godskriving_consumed_records", "status", "failed")
    private val counterProcessedRecords = registry.counter("omsorgp_godskriving_consumed_records", "status", "ok")

    @KafkaListener(
        containerFactory = "kafkaListenerContainerFactory",
        idIsGroup = false,
        topics = ["\${OMSORGP_GODSKRIVING_TOPIC}"],
        groupId = "\${OMSORGP_GODSKRIVING_GROUP_ID}"
    )
    fun consumeOmsorgPGodskriving(hendelse: String, consumerRecord: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        val record: OmsorgPGodskrivingRecord = jacksonObjectMapper().readValue(consumerRecord.value(), OmsorgPGodskrivingRecord::class.java)
        try {
            /*
            val brevbakingRequest = BrevbakingRequest(
                template = BrevKode.OMSORGP_GODSKRIVING.name,
                letterData = record.letterData,
                felles = Felles(
                    dokumentDato = LocalDate.now(),
                    saksnummer = record.saksnummer
                ),
                language =)
            val journalforingInfo = JournalforingInfo()
            val brevDistribueringsInfo = BrevDistribueringsInfo()

            journalforingService.journalfor(
                brevbakingRequest = brevbakingRequest,
                journalforingInfo = journalforingInfo,
                brevDistribueringsInfo = brevDistribueringsInfo
            )
            acknowledgment.acknowledge()
            counterProcessedRecords.increment()

             */
        } catch (e: Exception) {
            counterFailedRecords.increment()
            logger.error(e.message, e)
            throw e
        }
    }


    @PostConstruct
    fun initMetrics() {
        logger.info("Starting to listen to topic $topic")
        counterStartingListener.increment()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgPGodskrivingListener::class.java)
    }
}