package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode


class OpprettJournalpostRequest(
    val avsenderMottaker: Mottaker,
    val behandlingstema: Behandlingstema,
    val bruker: Bruker,
    val dokumenter: List<Dokument>,
    val journalfoerendeEnhet: Enhet = Enhet.AUTOMATISK_JOURNALFORING,
    val journalpostType: JournalpostType = JournalpostType.UTGAAENDE,
    val sak: Sak,
    val tema: Tema,
    val tittel: String,
    val kanal: String = "POPP", //TODO finn ut hva denne skal være
    val eksternReferanseId: String? = null,
    val tilleggsopplysninger: Tilleggsopplysning? = null,
) {
    init {
        require(dokumenter.isNotEmpty()) { "OpprettJournalpostRequest.dokumenter must have one document" }
    }
}

data class Mottaker(
    val id: String? = null, //fnr
    val land: String? = null, //må oppgis hvis bruker ie
    val idType: IdType = IdType.FNR,
)

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
    val idType: IdType = IdType.FNR,
)

enum class IdType { FNR }

data class Dokument(
    val brevkode: BrevKode,
    val tittel: String,
    val dokumentvarianter: List<Dokumentvariant>,
)

data class Dokumentvariant(
    val fysiskDokument: ByteArray,
    val filtype: Filtype = Filtype.PDFA,
    val variantformat: Variantformat = Variantformat.ARKIV,
)

//TODO
enum class Filtype { PDFA }

//TODO
enum class Variantformat { ARKIV }

//TODO hør med fagperson om hva vi skal velge
enum class Enhet(@JsonValue val enhetsNr: String) {
    AUTOMATISK_JOURNALFORING("9999"),
    DISKRESJONSKODE("2103"); //Vikafossen strengt fortrolig SPSF
}

enum class JournalpostType { UTGAAENDE }

data class Sak(
    val arkivsaksnummer: String,
    val arkivsaksystem: Fagsaksystem = Fagsaksystem.PP01,
    val sakstype: SaksType = SaksType.FAGSAK,
)

enum class SaksType {
    FAGSAK
}

enum class Fagsaksystem {
    PP01
}

//TODO hør med fagperson om hva vi skal velge
enum class Tema { PEN, UFO }

@JsonIgnoreProperties(ignoreUnknown = true)
class OpprettJournalPostResponse(
    val journalpostId: String,
    val journalstatus: String,
    val melding: String? = null,
    val journalpostferdigstilt: Boolean,
)

data class Tilleggsopplysning(
    val nokkel: String,
    val verdi: String,
)









