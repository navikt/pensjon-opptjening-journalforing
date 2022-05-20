package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.interceptor.TokenInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.client.RestTemplate
import pensjon.opptjening.azure.ad.client.AzureAdTokenProvider
import pensjon.opptjening.azure.ad.client.AzureAdVariableConfig
import pensjon.opptjening.azure.ad.client.TokenProvider
import java.time.Duration

@Configuration
class KrrClientConfig {
    @Bean("azureAdConfigKrr")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigKrr(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${KRR_API_ID}") krrApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = krrApiId,
        wellKnownUrl = wellKnownUrl
    )

    @Bean("tokenProviderKrr")
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderKrr(@Qualifier("azureAdConfigKrr") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("krrTokenInterceptor")
    fun krrTokenInterceptor(@Qualifier("tokenProviderKrr") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("krrRestTemplate")
    fun journalforingRestTemplate(@Value("\${JOURNALFORING_URL}") url: String, @Qualifier("krrTokenInterceptor") tokenInterceptor: TokenInterceptor): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()

    @Bean
    fun krrClient(@Qualifier("krrRestTemplate") restTemplate: RestTemplate, @Value("\${KRR_PROXY_URL}") url: String, registry: MeterRegistry): KrrClient {
        return KrrClient(restTemplate, url, registry)
    }
}
