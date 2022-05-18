package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import com.fasterxml.jackson.annotation.JsonValue
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode

class OpprettJournalpostRequest(
    val avsenderMottaker: Avsender,
    val behandlingstema: Behandlingstema,
    val bruker: Bruker,
    val dokumenter: List<Dokument>,
    val journalfoerendeEnhet: Enhet = defaultEnhet,
    val journalposttype: Journalposttype = defaultJournalposttype,
    val sak: Sak,
    val tema: Tema,
    val tittel: String,
    val kanal: String = defaultKanal, //TODO finn ut hva denne skal være
    val eksternReferanseId: String? = null,
    val tilleggsopplysninger: List<Tilleggsopplysning>? = null,
) {
    init {
        require(dokumenter.isNotEmpty()) { "OpprettJournalpostRequest.dokumenter must have one document" }
    }

    companion object{
        val defaultEnhet = Enhet.AUTOMATISK_JOURNALFORING
        val defaultJournalposttype = Journalposttype.UTGAAENDE
        val defaultKanal = "POPP"
    }
}

data class Avsender(
    val id: String = navOrgNr,
    val land: String = norge,
    val idType: IdType = orgIdType,
    val navn:String = nav,
){
    companion object{
        val orgIdType = IdType.ORGNR
        val navOrgNr = "889640782"
        val norge = "NO"
        val nav = "NAV"
    }
}


//TODO hør med fagperson om hva vi skal velge
enum class Behandlingstema(@JsonValue val kode: String) {
    OMSORGSPENGER("ab0149"),
    OMSORGSPENGER_PLEIEPENGER_OPPLAERINGSPENGER("ab0271"),
    OMSORGSPOENG("ab0341"),
    BARNETRYGD("ab0270"),
    BARNETRYGD_EOS("ab0058"),
    BARNETRYGD_UTVIDET("ab0096"),
    BARNETRYGD_ORDINAER("ab0180"),
}

data class Bruker(
    val id: String,
    val idType: IdType = defaultMottakerIdType,
){
    companion object{
        val defaultMottakerIdType = IdType.FNR
    }
}

enum class IdType { FNR , ORGNR}

data class Dokument(
    val brevkode: BrevKode,
    val tittel: String,
    val dokumentvarianter: List<Dokumentvariant>,
)

data class Dokumentvariant(
    val fysiskDokument: String,
    val filtype: Filtype = defaultFiltype,
    val variantformat: Variantformat = defaultVariantformat,
){
    companion object{
        val defaultFiltype: Filtype = Filtype.PDFA
        val defaultVariantformat: Variantformat = Variantformat.ARKIV
    }
}

//TODO
enum class Filtype { PDFA }

//TODO
enum class Variantformat { ARKIV }

//TODO hør med fagperson om hva vi skal velge
enum class Enhet(@JsonValue val enhetsNr: String) {
    AUTOMATISK_JOURNALFORING("9999"),
    DISKRESJONSKODE("2103"); //Vikafossen strengt fortrolig SPSF
}

enum class Journalposttype { UTGAAENDE }

data class Sak(
    val fagsakId: String,
    val fagsaksystem: Fagsaksystem = defaultFagsaksystem,
    val sakstype: SaksType = defaultSakstype,
){
    companion object{
        val defaultFagsaksystem: Fagsaksystem = Fagsaksystem.PP01
        val defaultSakstype : SaksType = SaksType.FAGSAK
    }
}

enum class SaksType {
    FAGSAK
}

enum class Fagsaksystem {
    PP01
}

//TODO hør med fagperson om hva vi skal velge
enum class Tema { PEN, UFO }

data class Tilleggsopplysning(
    val nokkel: String,
    val verdi: String,
)