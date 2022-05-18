package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.Md5Hash
import org.springframework.web.client.RestTemplate
import java.util.*

class JournalforingClient(private val restTemplate: RestTemplate, private val url: String) {

    fun opprettJournalpost(journalpostInfo: JournalpostInfo) {

        val request: OpprettJournalpostRequest = OpprettJournalpostRequest(
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
}

data class JournalpostInfo(
    val fnr: String,
    val ar: Int,
    val sak: Sak,
    val sakType: SakType,
    val land: String,
    private val brevbakingResponse: LetterResponse,
    val brevKode: BrevKode,
) {

    fun brevTittel() = brevbakingResponse.letterMetadata.displayTitle

    fun brev() = Base64.getDecoder().decode(brevbakingResponse.base64pdf)

    fun tilleggsopplysning() = Tilleggsopplysning("isSensitiv", brevbakingResponse.letterMetadata.isSensitiv.toString())

    fun unikBrevId() = "${brevKode.name}${Md5Hash.createHashString("${brevKode.name}$fnr$ar${sak.arkivsaksnummer}")}" // Vi må urdere denne logikken

    fun getTema(): Tema = sakType.getTema()

    fun getBehandlingsTema(): Behandlingstema = sakType.getBehandlingsTema()

}

//TODO sjekk hva denne skal være
enum class SakType {
    OMSORG;

    fun getTema(): Tema = when (this) {
        OMSORG -> OmsorgsTema().getTema()
    }

    fun getBehandlingsTema(): Behandlingstema = when (this) {
        OMSORG -> OmsorgsTema().getBehandlingsTema()
    }
}


class OmsorgsTema : TemaInfo {
    override fun getTema(): Tema = Tema.PEN // finn riktig tema
    override fun getBehandlingsTema(): Behandlingstema = Behandlingstema.OMSORGSPENGER // todo er denne riktig
}

interface TemaInfo {
    fun getTema(): Tema
    fun getBehandlingsTema(): Behandlingstema
}