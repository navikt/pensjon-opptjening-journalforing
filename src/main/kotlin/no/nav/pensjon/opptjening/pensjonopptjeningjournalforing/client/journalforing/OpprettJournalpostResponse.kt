package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

data class OpprettJournalpostResponse(
    val journalpostId: String,
    val journalpostferdigstilt: Boolean,
    val dokumenter: List<DokumentInfoId>? = null,
    val melding: String? = null,
)

data class DokumentInfoId(
    val dokumentInfoId: String
)