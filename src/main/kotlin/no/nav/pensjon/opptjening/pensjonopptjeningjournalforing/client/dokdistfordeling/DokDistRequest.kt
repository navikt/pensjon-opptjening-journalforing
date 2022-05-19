package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.BrevDistribueringsInfo

data class DokDistRequest(
    val journalpostId: String,        // "Journalpost som skal distribueres", example = "343752389"
    val bestillendeFagsystem: String, // "Fagsystemet som bestiller distribusjon", example = "SYM"
    val batchId: String? = null,      // "Identifiserer batch som forsendelsen inngår i. Lar bestiller identifisere forsendelser som hører sammen. Fritekst, og konsument må selv vurdere hva som er hensiktsmessige verdier", example = "54321"
    val adresse: Adresse? = null,             // "Struktur for å beskrive postadresse. Inneholder enten norsk postadresse eller utenlandsk postadresse. Påkrevd hvis mottaker er samhandler, ellers skal dokdistsentralprint hente adresse fra fellesregistre hvis ikke satt
    val dokumentProdApp: String,      //  Applikasjon som har produsert hoveddokumentet (for sporing og feilsøking)
    val distribusjonstype: Distribusjonstype,   // Forteller dokumentdistribusjon hva slags dokument som distribueres. \"VEDTAK\", \"VIKTIG\" eller \"ANNET\""
    val distribusjonstidspunkt: Distribusjonstidspunkt? = null, // Forteller dokumentdistribusjon når dokumentet kan distribueres.
) {
    constructor(brevDistribueringsInfo: BrevDistribueringsInfo, journalpostId: String) : this(
        journalpostId,
        brevDistribueringsInfo.bestillendeFagsystem,
        brevDistribueringsInfo.batchId,
        brevDistribueringsInfo.adresse,
        brevDistribueringsInfo.dokumentProdApp,
        brevDistribueringsInfo.distribusjonstype,
        brevDistribueringsInfo.distribusjonstidspunkt
    )
}

data class Adresse(
    val adressetype: Adressetype,
    val postnummer: String? = null,
    val poststed: String? = null,
    val adresselinje1: String? = null,
    val adresselinje2: String? = null,
    val adresselinje3: String? = null,
    val land: String? = null, // To-bokstavers landkode ihht iso3166-1 alfa-2 (NO)
) {

    init {
        require(!(adressetype == Adressetype.norskPostadresse && postnummer == null)) { "Adresse.postnummer er Påkrevd hvis adressetype er ${Adressetype.norskPostadresse.name}" }
        require(!(adressetype == Adressetype.norskPostadresse && poststed == null)) { "Adresse.poststed er Påkrevd hvis adressetype er ${Adressetype.norskPostadresse.name}" }
        require(!(adressetype == Adressetype.utenlandskPostadresse && adresselinje1 == null)) { "Adresse.adresselinje1 er Påkrevd hvis adressetype er ${Adressetype.utenlandskPostadresse.name}" }
    }
}

enum class Adressetype {
    norskPostadresse,
    utenlandskPostadresse
}

enum class Distribusjonstype {
    VEDTAK,
    VIKTIG,
    ANNET
}

enum class Distribusjonstidspunkt {
    UMIDDELBART,
    KJERNETID
}



