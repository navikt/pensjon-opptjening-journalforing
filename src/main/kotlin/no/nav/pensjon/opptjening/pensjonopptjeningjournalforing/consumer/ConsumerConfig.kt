package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import java.time.Duration

@EnableKafka
@Configuration
class KafkaConfigProd(
    @Value("\${kafka.keystore.path}") private val keystorePath: String,
    @Value("\${kafka.credstore.password}") private val credstorePassword: String,
    @Value("\${kafka.truststore.path}") private val truststorePath: String,
    @Value("\${kafka.brokers}") private val aivenBootstrapServers: String,
    @Value("\${kafka.security.protocol}") private val securityProtocol: String,
) {

    private val consumerConfig
        get(): Map<String, Any> =
            mapOf(
                ConsumerConfig.CLIENT_ID_CONFIG to "pensjon-opptjening-journalforing-v1",
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to aivenBootstrapServers,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 1,
            )

    @Bean
    fun kafkaListener(consumerFactoryBrev: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String>? =
        ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = consumerFactoryBrev
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
            containerProperties.setAuthExceptionRetryInterval(Duration.ofSeconds(4L))
        }

    @Bean
    fun kafkaConsumerFactory(@Qualifier("securityConfig") securityConfig: Map<String, String>): ConsumerFactory<String, String> =
        DefaultKafkaConsumerFactory(consumerConfig + securityConfig, StringDeserializer(), StringDeserializer())

    @Bean("securityConfig")
    fun securityConfig(): Map<String, String> =
        mapOf(
            SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG to keystorePath,
            SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG to credstorePassword,
            SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG to credstorePassword,
            SslConfigs.SSL_KEY_PASSWORD_CONFIG to credstorePassword,
            SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG to "JKS",
            SslConfigs.SSL_KEYSTORE_TYPE_CONFIG to "PKCS12",
            SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG to truststorePath,
            CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to securityProtocol,
        )
}

