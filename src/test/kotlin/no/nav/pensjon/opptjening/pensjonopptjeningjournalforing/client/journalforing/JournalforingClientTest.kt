package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterMetadata
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalpostInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.SakType
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
internal class JournalforingClientTest {
    @Autowired
    private lateinit var journalforingClient: JournalforingClient

    @Test
    fun `should call journalforing with expected json`() {
        val response = OpprettJournalpostResponse(journalpostId = "3456", journalpostferdigstilt = false)

        WireMock.stubFor(
            WireMock.post(WireMock.urlEqualTo("/"))
                .willReturn(
                    WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(response.toJson())
                   )
        )

        val request = JournalpostInfo(
            fnr = "12345678901",
            ar = 2020,
            sak = Sak(
                arkivsaksnummer = "33333",
                arkivsaksystem = Fagsaksystem.PP01,
                sakstype = SaksType.FAGSAK
            ),
            sakType = SakType.OMSORG,
            land = "NO",
            brevbakingResponse = LetterResponse(
                base64pdf = "fasdsa",
                letterMetadata = LetterMetadata(
                    displayTitle = "Test title",
                    isSensitiv = false)
            ),
            brevKode = BrevKode.OMSORGP_GODSKRIVING
        )

        journalforingClient.opprettJournalpost(request)

        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.JOURNALFORING_TOKEN}"))
            //.withRequestBody(WireMock.equalToJson(exampleRequest))
        )
    }
}



private val exampleRequest = """
    
""".trimIndent()