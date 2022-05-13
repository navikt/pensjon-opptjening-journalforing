package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestKafkaConfig {
    @Bean("securityConfig")
    fun securityConfig(): Map<String, String> = mapOf()
}