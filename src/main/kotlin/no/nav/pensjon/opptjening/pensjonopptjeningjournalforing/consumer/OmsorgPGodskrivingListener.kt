package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer

import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class OmsorgPGodskrivingListener(registry: MeterRegistry) {

    @KafkaListener(
        containerFactory = "kafkaListenerContainerFactory",
        topics = ["\${OMSORGP_GODSKRIVING_TOPIC}"],
        groupId = "\${OMSORGP_GODSKRIVING_GROUP_ID}"
    )
    fun consumeOmsorgPGodskriving(cr: ConsumerRecord<String, String>, acknowledgment: Acknowledgment, @Payload melding: String) {
        try {
            logger.info("mottatt omsorgPGodskriving melding : $melding")
            acknowledgment.acknowledge()
        } catch (ex: Exception) {
            logger.error("Noe gikk galt under journalforing av omsorgPGodskriving:\n $melding \n ${ex.message}", ex)
            throw RuntimeException(ex.message)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgPGodskrivingListener::class.java)
    }
}