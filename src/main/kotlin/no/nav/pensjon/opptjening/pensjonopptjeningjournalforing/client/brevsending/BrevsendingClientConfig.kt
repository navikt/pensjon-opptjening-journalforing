package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevsending

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
class BrevsendingClientConfig {

    @Bean("azureAdConfigBrevsending")
    @Profile("dev-fss", "prod-fss")
    fun azureAdConfigBrevsending(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${BREVSENDING_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl
    )

    @Bean("tokenProviderBrevsending")
    @Profile("dev-fss", "prod-fss")
    fun tokenProviderBrevsending(@Qualifier("azureAdConfigBrevsending") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("brevsendingTokenInterceptor")
    fun brevsendingTokenInterceptor(@Qualifier("tokenProviderBrevsending") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("brevsendingRestTemplate")
    fun brevsendingRestTemplate(@Value("\${BREVSENDING_URL}") url: String, @Qualifier("brevsendingTokenInterceptor") tokenInterceptor: TokenInterceptor): RestTemplate = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()
}