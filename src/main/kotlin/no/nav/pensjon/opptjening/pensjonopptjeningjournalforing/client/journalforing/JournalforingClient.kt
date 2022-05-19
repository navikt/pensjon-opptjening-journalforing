package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalforingInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class JournalforingClient(private val restTemplate: RestTemplate, url: String) {

    private val urlWithParams = UriComponentsBuilder
        .fromUriString(url)
        .queryParam("forsoekFerdigstill", "true")
        .build()
        .toUri()

    fun opprettJournalpost(journalforingInfo: JournalforingInfo, brevbakingResponse: LetterResponse): OpprettJournalpostResponse {
        val request = createRequest(journalforingInfo,brevbakingResponse)

        return restTemplate.exchange(
            urlWithParams,
            HttpMethod.POST,
            HttpEntity(request.toJson(), applicationJsonHeader),
            OpprettJournalpostResponse::class.java
        ).body!!
    }

    private fun createRequest(journalforingInfo: JournalforingInfo, brevbakingResponse: LetterResponse) =
        OpprettJournalpostRequest(
            avsenderMottaker = Avsender(),
            behandlingstema = journalforingInfo.getBehandlingsTema(),
            tema = journalforingInfo.getTema(),
            bruker = Bruker(id = journalforingInfo.fnr),
            dokumenter = listOf(
                Dokument(
                    tittel = brevbakingResponse.brevTittel(),
                    brevkode = journalforingInfo.brevKode,
                    dokumentvarianter = listOf(Dokumentvariant(fysiskDokument = brevbakingResponse.brev()))
                )
            ),
            sak = journalforingInfo.sak,
            tittel = brevbakingResponse.brevTittel(),
            eksternReferanseId = journalforingInfo.unikBrevId(),
            tilleggsopplysninger = brevbakingResponse.tilleggsopplysning()
        )

    companion object {
        private val applicationJsonHeader = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
    }
}