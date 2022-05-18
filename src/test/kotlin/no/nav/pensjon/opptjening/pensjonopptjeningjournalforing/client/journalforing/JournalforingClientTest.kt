package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterMetadata
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClientTest.Companion.AR
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClientTest.Companion.FNR
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClientTest.Companion.PDF
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClientTest.Companion.SAK_ID
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.JournalforingClientTest.Companion.TITTEL
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalpostInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.OmsorgsTema
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.SakType
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.Md5Hash
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
            fnr = FNR,
            ar = AR,
            sak = Sak(fagsakId = SAK_ID),
            sakType = SakType.OMSORG,
            land = "NO",
            brevbakingResponse = LetterResponse(
                base64pdf = PDF,
                letterMetadata = LetterMetadata(
                    displayTitle = TITTEL,
                    isSensitiv = false)
            ),
            brevKode = BrevKode.OMSORGP_GODSKRIVING
        )

        journalforingClient.opprettJournalpost(request)

        WireMock.verify(1, WireMock.postRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.JOURNALFORING_TOKEN}"))
            .withRequestBody(WireMock.equalToJson(OMSORGP_GODSKRIVING_REQUEST))
        )
    }

    companion object {
        const val FNR = "09071844797"
        const val AR = 2020
        const val SAK_ID = "3333"
        const val TITTEL = "Test title"
        const val PDF = "DummyPdf"
    }
}


private val OMSORGP_GODSKRIVING_REQUEST = """
{
  "avsenderMottaker": {
    "id": "${Avsender.navOrgNr}",
    "land": "${Avsender.norge}",
    "idType": "${Avsender.orgIdType}",
    "navn": "${Avsender.nav}"
  },
  "behandlingstema": "${OmsorgsTema().getBehandlingsTema().kode}",
  "bruker": {
    "id": "$FNR",
    "idType": "${Bruker.defaultMottakerIdType}"
  },
  "dokumenter": [
    {
      "brevkode": "${BrevKode.OMSORGP_GODSKRIVING}",
      "tittel": "$TITTEL",
      "dokumentvarianter": [
        {
          "fysiskDokument": "$PDF",
          "filtype": "${Dokumentvariant.defaultFiltype}",
          "variantformat": "${Dokumentvariant.defaultVariantformat}"
        }
      ]
    }
  ],
  "journalfoerendeEnhet": "${OpprettJournalpostRequest.defaultEnhet.enhetsNr}",
  "journalposttype": "${OpprettJournalpostRequest.defaultJournalposttype}",
  "sak": {
    "fagsakId": "$SAK_ID",
    "fagsaksystem": "${Fagsaksystem.PP01}",
    "sakstype": "${SaksType.FAGSAK}"
  },
  "tema": "${OmsorgsTema().getTema()}",
  "tittel": "$TITTEL",
  "kanal": "${OpprettJournalpostRequest.defaultKanal}",
  "eksternReferanseId": "${BrevKode.OMSORGP_GODSKRIVING.name}${Md5Hash.createHashString("$FNR$AR$SAK_ID")}",
  "tilleggsopplysninger": [
    {
      "nokkel": "isSensitiv",
      "verdi": "false"
    }
  ]
}
"""