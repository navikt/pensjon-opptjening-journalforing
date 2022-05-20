package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode


class KrrResponse(
    val personident: String,
    val aktiv: Boolean,
    val spraak: String? = null,
)

fun KrrResponse.getLanguageCode(): LanguageCode {
    return LanguageCode.createLanguageCode(spraak ?: "default")
}