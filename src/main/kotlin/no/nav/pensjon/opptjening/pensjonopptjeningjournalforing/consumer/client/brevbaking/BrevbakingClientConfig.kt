package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.interceptor.TokenInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import pensjon.opptjening.azure.ad.client.AzureAdTokenProvider
import pensjon.opptjening.azure.ad.client.AzureAdVariableConfig
import pensjon.opptjening.azure.ad.client.TokenProvider
import java.time.Duration

@Configuration
class BrevbakingClientConfig {

    @Bean("azureAdConfigBrevbaking")
    @Profile("dev-fss", "prod-fss")
    fun azureAdConfigBrevbaking(
        @Value("\${AZURE_APP_CLIENT_ID}") azureAppClientId: String,
        @Value("\${AZURE_APP_CLIENT_SECRET}") azureAppClientSecret: String,
        @Value("\${BREVBAKING_API_ID}") pgiEndringApiId: String,
        @Value("\${AZURE_APP_WELL_KNOWN_URL}") wellKnownUrl: String,
    ) = AzureAdVariableConfig(
        azureAppClientId = azureAppClientId,
        azureAppClientSecret = azureAppClientSecret,
        targetApiId = pgiEndringApiId,
        wellKnownUrl = wellKnownUrl,
    )

    @Bean("tokenProviderBrevBaking")
    @Profile("dev-fss", "prod-fss")
    fun tokenProviderBrevBaking(@Qualifier("azureAdConfigBrevbaking") azureAdVariableConfig: AzureAdVariableConfig): TokenProvider = AzureAdTokenProvider(azureAdVariableConfig)

    @Bean("brevbakingTokenInterceptor")
    fun brevbakingTokenInterceptor(@Qualifier("tokenProviderBrevBaking") tokenProvider: TokenProvider): TokenInterceptor = TokenInterceptor(tokenProvider)

    @Bean("brevbakingRestTemplate")
    fun brevbakingRestTemplate(@Value("\${BREVBAKING_URL}") url: String, @Qualifier("brevbakingTokenInterceptor") tokenInterceptor: TokenInterceptor) = RestTemplateBuilder()
        .setConnectTimeout(Duration.ofMillis(1000))
        .rootUri(url)
        .additionalInterceptors(tokenInterceptor)
        .build()
}