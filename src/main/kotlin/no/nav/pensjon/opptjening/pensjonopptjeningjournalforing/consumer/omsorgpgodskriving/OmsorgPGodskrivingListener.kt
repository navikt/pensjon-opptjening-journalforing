package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.omsorgpgodskriving

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.Felles
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.*
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
            journalforingService.journalfor(
                brevInfo = createBrevbakingInfo(record.letterData,record.saksnummer),
                journalforingInfo = JournalforingInfo(),
                brevDistribueringsInfo = BrevDistribueringsInfo()
            )

             */

            acknowledgment.acknowledge()
            counterProcessedRecords.increment()
        } catch (e: Exception) {
            counterFailedRecords.increment()
            logger.error(e.message, e)
            throw e
        }
    }

    private fun createBrevbakingInfo(letterData: String, saksnummer: String) =
        BrevInfo(
            template = BrevKode.OMSORGP_GODSKRIVING.name,
            letterData = letterData,
            felles = Felles(
                dokumentDato = LocalDate.now(),
                saksnummer = saksnummer
            ))

    @PostConstruct
    fun initMetrics() {
        logger.info("Starting to listen to topic $topic")
        counterStartingListener.increment()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgPGodskrivingListener::class.java)
    }
}