package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking

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
class BrevbakerClientConfig {

    @Bean("azureAdConfigBrevbaker")
    @Profile("dev-fss", "prod-fss")
    fun azureAdConfigBrevbaker(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${BREVBAKER_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean("tokenProviderBrevbaker")
    @Profile("dev-fss", "prod-fss")
    fun tokenProviderBrevbaker(@Qualifier("azureAdConfigBrevbaker") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("brevbakerTokenInterceptor")
    fun brevbakerTokenInterceptor(@Qualifier("tokenProviderBrevbaker") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("brevbakerRestTemplate")
    fun brevbakerRestTemplate(@Value("\${BREVBAKER_URL}") url: String, @Qualifier("brevbakerTokenInterceptor") tokenInterceptor: TokenInterceptor) = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()

    @Bean
    fun brevbakerClient(@Qualifier("brevbakerRestTemplate") restTemplate: RestTemplate,@Value("\${BREVBAKER_URL}") url: String): BrevbakerClient {
        return BrevbakerClient(restTemplate,url)
    }
}