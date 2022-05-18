package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Behandlingstema
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Sak
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Tema
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Tilleggsopplysning
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.Md5Hash

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

    fun brev() = brevbakingResponse.base64pdf

    fun tilleggsopplysning() = listOf(Tilleggsopplysning("isSensitiv", brevbakingResponse.letterMetadata.isSensitiv.toString()))

    fun unikBrevId() = "${brevKode.name}${Md5Hash.createHashString("$fnr$ar${sak.fagsakId}")}" // Vi må urdere denne logikken

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