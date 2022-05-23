package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.HttpStatusCodeException

@SpringBootTest
@WireMockTest(httpPort = 4567)
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pgi-endring-topic"])
internal class KrrClientTest {
    @Autowired
    private lateinit var krrClient: KrrClient

    @Test
    fun `should call krrProxy with required headers Nav-Personident, token and Nav-Call-Id `() {
        stubKrr(krrResponse = KrrResponse(personident = FNR, aktiv = false, spraak = "nb"))

        krrClient.getLanguageCode(FNR)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.KRR_TOKEN}"))
            .withHeader("Nav-Personident", WireMock.equalTo(FNR))
            .withHeader("Nav-Call-Id", WireMock.matching("(.*?)"))
        )
    }

    @ParameterizedTest(name = "{index} => spraak={0}, languageCode={1}")
    @CsvSource(
        "en, ENGLISH",
        "En, ENGLISH",
        "eN, ENGLISH",
        "EN, ENGLISH",
        "nn, NYNORSK",
        "nN, NYNORSK",
        "Nn, NYNORSK",
        "NN, NYNORSK",
        "nb, BOKMAL",
        "Nb, BOKMAL",
        "nB, BOKMAL",
        "NB, BOKMAL",
        "Random, BOKMAL"
    )
    fun `spraak equal to en should translate to LanguageCode ENGLISH`(spraak: String, languageCode: String) {
        stubKrr(KrrResponse(FNR, true, spraak))

        assertEquals(LanguageCode.valueOf(languageCode), krrClient.getLanguageCode(FNR))
    }

    @Test
    fun `should return BOKMAL if 404 NOT FOUND is returned from krr `() {
        stubKrr(krrResponse = null, HttpStatus.NOT_FOUND)

        assertEquals(LanguageCode.BOKMAL, krrClient.getLanguageCode(FNR))
    }

    @Test
    fun `should retry three times and throw exception if krr returns 500 server error`() {
        stubKrr(krrResponse = null, HttpStatus.INTERNAL_SERVER_ERROR)

        assertThrows<HttpStatusCodeException> {
            krrClient.getLanguageCode(FNR)
        }

        WireMock.verify(3, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/")))
    }

    private fun stubKrr(krrResponse: KrrResponse?, status: HttpStatus = HttpStatus.OK) {
        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/"))
                .willReturn(
                    WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(status.value())
                        .withBody(krrResponse?.toJson())
                )
        )
    }

    companion object {
        const val FNR = "12345678901"
    }
}