package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking.model

import java.time.LocalDate


data class LetterRequest(val template: String, val letterData: Any, val felles: Felles, val language: LanguageCode)

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;
}

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet,
    val mottaker: Mottaker,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
)

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String)

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String)

data class Mottaker(
    val fornavn: String,
    val mellomnavn: String? = null,
    val etternavn: String,
    val foedselsnummer: Foedselsnummer,
    val adresse: Adresse,
)

data class Adresse(
    val linje1: String,
    val linje2: String,
    val linje3: String? = null,
    val linje4: String? = null,
    val linje5: String? = null,
)

data class NAVEnhet(
    val returAdresse: ReturAdresse,
    val nettside: String,
    val navn: String,
    val telefonnummer: Telefonnummer,
)

data class Telefonnummer(val value: String)
data class Foedselsnummer(val value: String)