package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pensjon.opptjening.azure.ad.client.TokenProvider
import pensjon.opptjening.azure.ad.client.mock.MockTokenProvider

@Configuration
class MockTokenConfig {

    companion object {
        const val BREVBAKING_TOKEN = "BrevBaking.BrevBaking.BrevBaking"
        const val DOKDISTFORDELING_TOKEN = "dokdistfordeling.dokdistfordeling.dokdistfordeling"
        const val JOURNALFORING_TOKEN = "Journalforing.Journalforing.Journalforing"
    }

    @Bean("tokenProviderBrevbaker")
    fun mockTokenProviderBrevBaking(): TokenProvider = MockTokenProvider(BREVBAKING_TOKEN)

    @Bean("tokenProviderDokdistfordeling")
    fun mockTokenProviderDokdistfordeling(): TokenProvider = MockTokenProvider(DOKDISTFORDELING_TOKEN)

    @Bean("tokenProviderJournalforing")
    fun mockTokenProviderJournalforing(): TokenProvider = MockTokenProvider(JOURNALFORING_TOKEN)

}