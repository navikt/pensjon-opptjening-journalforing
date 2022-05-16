package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling

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
class DokdistClientConfig {

    @Bean("azureAdConfigDokdist")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigDokdist(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${DOKDISTFORDELING_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl
    )

    @Bean("tokenProviderDokdist")
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderDokdist(@Qualifier("azureAdConfigDokdist") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider =
        AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("dokdistTokenInterceptor")
    fun dokdistTokenInterceptor(@Qualifier("tokenProviderDokdist") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("dokdistRestTemplate")
    fun dokdistRestTemplate(@Value("\${DOKDISTFORDELING_URL}") url: String, @Qualifier("dokdistTokenInterceptor") tokenInterceptor: TokenInterceptor) =
        RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(1000))
            .rootUri(url)
            .additionalInterceptors(tokenInterceptor)
            .build()

    @Bean
    fun dokDistClient(@Value("\${DOKDISTFORDELING_URL}") url: String, @Qualifier("dokdistRestTemplate") dokdistRestTemplate: RestTemplate): DokDistClient =
        DokDistClient(url, dokdistRestTemplate)
}