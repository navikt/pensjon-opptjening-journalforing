package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler
import org.springframework.kafka.listener.ContainerAwareErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class KafkaStoppingErrorHandler : ContainerAwareErrorHandler {
    private val logger = LoggerFactory.getLogger(KafkaStoppingErrorHandler::class.java)
    private val stopper = CommonContainerStoppingErrorHandler()

    override fun handle(
        thrownException: Exception,
        records: MutableList<ConsumerRecord<*, *>>?,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer,
    ) {
        logger.error(
            "En feil oppstod under kafka konsumering av meldinger: \n${textList(records)} \nStopper containeren ! Restart er nødvendig for å fortsette konsumering",
            thrownException
        )
        stopper.handleRemaining(thrownException, records ?: emptyList(), consumer, container)
    }

    fun textList(records: List<ConsumerRecord<*, *>>?) =
        records?.joinToString(separator = "\n") {
            "--------------------------------------------------------------------------------\n$it"
        } ?: "No records"

}

