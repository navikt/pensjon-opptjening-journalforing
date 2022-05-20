package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode


class KrrResponse(
    val personer: Map<String, DigitalKontaktinformasjon> = mapOf(),
    val feil: Map<String, String> = mapOf(),
)

class DigitalKontaktinformasjon(
    val personident: String,
    val aktiv: Boolean,
    val spraak: String? = null,
)

fun KrrResponse.getLanguageCode(): LanguageCode = LanguageCode.createLanguageCode(personer.entries.firstOrNull()?.value?.spraak ?: "default")