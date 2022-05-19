package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.stereotype.Component

@Component
class KafkaStoppingErrorHandler : CommonContainerStoppingErrorHandler() {
    private val logger = LoggerFactory.getLogger(KafkaStoppingErrorHandler::class.java)

    override fun handleRemaining(thrownException: Exception, records: MutableList<ConsumerRecord<*, *>>, consumer: Consumer<*, *>, container: MessageListenerContainer) {
        createAlert()
        logError(thrownException,records)
        super.handleRemaining(thrownException, records, consumer, container)
    }

    private fun createAlert(){
        //TODO
    }

    private fun logError(thrownException: Exception, records: MutableList<ConsumerRecord<*, *>>){
        logger.error(
            "En feil oppstod under kafka konsumering av meldinger: \n${textList(records)} \nStopper containeren ! Restart er nødvendig for å fortsette konsumering",
            thrownException
        )
    }

    private fun textList(records: List<ConsumerRecord<*, *>>?) =
        records?.joinToString(separator = "\n") {
            "--------------------------------------------------------------------------------\n$it"
        } ?: "No records"
}