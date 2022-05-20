package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@WireMockTest(httpPort = 4567)
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pgi-endring-topic"])
internal class KrrClientTest {
    @Autowired
    private lateinit var krrClient: KrrClient

    @Test
    fun `should call krrProxy`() {
        val fnr = "12345678901"

        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/"))
                .willReturn(
                    WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(KrrResponse(personident = fnr, aktiv = false, spraak = "nb").toJson())
                )
        )

        krrClient.getLanguageCode(fnr)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.KRR_TOKEN}"))
            .withHeader("Nav-Personident", WireMock.equalTo(fnr))
            .withHeader("Nav-Call-Id", WireMock.matching("(.*?)"))
        )
    }


}