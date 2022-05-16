package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.postadresse

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
class PostadresseClientConfig {

    @Bean("azureAdConfigPostadresse")
    @Profile("dev-gcp", "prod-gcp")
    fun azureAdConfigBrevbaker(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${POSTADRESSE_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean("tokenProviderPostadresse")
    @Profile("dev-gcp", "prod-gcp")
    fun tokenProviderPostadresse(@Qualifier("azureAdConfigBrevbaker") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("postadresseTokenInterceptor")
    fun postadresseTokenInterceptor(@Qualifier("tokenProviderPostadresse") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("postadresseRestTemplate")
    fun postadresseRestTemplate(@Value("\${POSTADRESSE_URL}") url: String, @Qualifier("postadresseTokenInterceptor") tokenInterceptor: TokenInterceptor) = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()

    @Bean
    fun postadresseClient(@Qualifier("postadresseRestTemplate") restTemplate: RestTemplate, @Value("\${POSTADRESSE_URL}") url: String): PostadresseClient {
        return PostadresseClient(restTemplate,url)
    }
}