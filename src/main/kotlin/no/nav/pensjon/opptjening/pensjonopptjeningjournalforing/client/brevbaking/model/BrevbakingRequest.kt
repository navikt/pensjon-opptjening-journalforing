package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.BrevInfo
import java.time.LocalDate


data class BrevbakingRequest(val template: String, val letterData: Any, val felles: Felles, val language: LanguageCode) {
    constructor(brevInfo: BrevInfo, language: LanguageCode) : this(
        template = brevInfo.template,
        letterData = brevInfo.letterData,
        felles = brevInfo.felles,
        language = language
    )
}

enum class LanguageCode {
    BOKMAL, NYNORSK, ENGLISH;

    companion object {
        fun createLanguageCode(spraak: String): LanguageCode {
            return when (spraak.uppercase()) {
                ("NN") -> NYNORSK
                ("NO") -> BOKMAL
                ("EN") -> ENGLISH
                else -> BOKMAL
            }
        }
    }
}

data class Felles(
    val dokumentDato: LocalDate,
    val saksnummer: String,
    val avsenderEnhet: NAVEnhet? = null,
    val mottaker: Mottaker? = null,
    val signerendeSaksbehandlere: SignerendeSaksbehandlere? = null,
)

data class ReturAdresse(val adresseLinje1: String, val postNr: String, val postSted: String)

data class SignerendeSaksbehandlere(val saksbehandler: String, val attesterendeSaksbehandler: String)

data class Mottaker(
    val fornavn: String,
    val mellomnavn: String? = null,
    val etternavn: String,
    val foedselsnummer: Foedselsnummer,
    val adresse: Adresse?,
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