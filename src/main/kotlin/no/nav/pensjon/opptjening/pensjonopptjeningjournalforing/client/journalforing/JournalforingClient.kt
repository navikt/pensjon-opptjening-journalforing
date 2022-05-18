package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalpostInfo
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

    fun opprettJournalpost(journalpostInfo: JournalpostInfo): OpprettJournalpostResponse {
        return restTemplate.exchange(
            urlWithParams,
            HttpMethod.POST,
            HttpEntity(createRequest(journalpostInfo).toJson(), applicationJsonHeader),
            OpprettJournalpostResponse::class.java
        ).body!!
    }

    private fun createRequest(journalpostInfo: JournalpostInfo) =
        OpprettJournalpostRequest(
            avsenderMottaker = Avsender(),
            behandlingstema = journalpostInfo.getBehandlingsTema(),
            tema = journalpostInfo.getTema(),
            bruker = Bruker(id = journalpostInfo.fnr),
            dokumenter = listOf(
                Dokument(
                    tittel = journalpostInfo.brevTittel(),
                    brevkode = journalpostInfo.brevKode,
                    dokumentvarianter = listOf(Dokumentvariant(fysiskDokument = journalpostInfo.brev()))
                )
            ),
            sak = journalpostInfo.sak,
            tittel = journalpostInfo.brevTittel(),
            eksternReferanseId = journalpostInfo.unikBrevId(),
            tilleggsopplysninger = journalpostInfo.tilleggsopplysning()
        )

    companion object {
        private val applicationJsonHeader = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
    }
}