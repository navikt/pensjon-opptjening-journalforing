package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.JournalpostInfo
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class JournalforingClient(private val restTemplate: RestTemplate, private val url: String) {

    fun opprettJournalpost(journalpostInfo: JournalpostInfo): OpprettJournalpostResponse {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        return restTemplate.exchange(
            url,
            HttpMethod.POST,
            HttpEntity(createRequest(journalpostInfo).toJson(), headers),
            OpprettJournalpostResponse::class.java).body!!
    }

    private fun createRequest(journalpostInfo: JournalpostInfo) =
        OpprettJournalpostRequest(
            avsenderMottaker = Mottaker(id = journalpostInfo.fnr, land = journalpostInfo.land, idType = IdType.FNR),
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
}