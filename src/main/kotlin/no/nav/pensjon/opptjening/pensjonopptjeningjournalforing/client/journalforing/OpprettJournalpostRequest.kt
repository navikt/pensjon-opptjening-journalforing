package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonValue


class OpprettJournalpostRequest(
    val avsenderMottaker: AvsenderMottaker,
    val behandlingstema: Behandlingstema,
    val bruker: Bruker,
    val dokumenter: List<Dokument>,
    val journalfoerendeEnhet: Enhet? = null,
    val journalpostType: JournalpostType = JournalpostType.UTGAAENDE,
    val sak: Sak,
    val tema: Tema = Tema.PEN,
    val tittel: String,
    val kanal: String = "EESSI", //TODO hva skal dette være
    val eksternReferanseId: String? = null,
) {
    init {
        require(dokumenter.isNotEmpty()) { "OpprettJournalpostRequest.dokumenter must have one document" }
    }
}

data class AvsenderMottaker(
    val id: String? = null,
    val idType: IdType? = null,
    val navn: String? = null,
    val land: String? = null,
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
    val brevkode: String,
    val tittel: String,
    val dokumentvarianter: List<Dokumentvariant>,
)

data class Dokumentvariant(
    val filtype: Filtype,
    val fysiskDokument: String,
    val variantformat: Variantformat,
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
    val arkivsaksystem: String,
)

//TODO hør med fagperson om hva vi skal velge
enum class Tema { PEN }

@JsonIgnoreProperties(ignoreUnknown = true)
class OpprettJournalPostResponse(
    val journalpostId: String,
    val journalstatus: String,
    val melding: String? = null,
    val journalpostferdigstilt: Boolean,
)









