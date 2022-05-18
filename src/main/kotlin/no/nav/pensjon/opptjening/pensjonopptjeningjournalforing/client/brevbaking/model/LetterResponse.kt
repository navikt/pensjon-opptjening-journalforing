package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model


data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata)

data class LetterMetadata(val displayTitle: String, val isSensitiv: Boolean)