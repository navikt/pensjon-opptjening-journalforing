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
        const val POSTADRESSE_TOKEN = "postadresse.postadresse.postadresse"
        const val KRR_TOKEN = "krr.krr.krr"
    }

    @Bean("tokenProviderBrevbaker")
    fun mockTokenProviderBrevBaking(): TokenProvider = MockTokenProvider(BREVBAKING_TOKEN)

    @Bean("tokenProviderDokdist")
    fun mockTokenProviderDokdistfordeling(): TokenProvider = MockTokenProvider(DOKDISTFORDELING_TOKEN)

    @Bean("tokenProviderJournalforing")
    fun mockTokenProviderJournalforing(): TokenProvider = MockTokenProvider(JOURNALFORING_TOKEN)

    @Bean("tokenProviderPostadresse")
    fun mockTokenProviderPostadresse(): TokenProvider = MockTokenProvider(POSTADRESSE_TOKEN)

    @Bean("tokenProviderKrr")
    fun mockTokenProviderKrr(): TokenProvider = MockTokenProvider(KRR_TOKEN)

}