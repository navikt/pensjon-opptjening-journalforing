package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing.Tilleggsopplysning


data class LetterResponse(val base64pdf: String, val letterMetadata: LetterMetadata){
    fun brevTittel() = letterMetadata.displayTitle

    fun brev() = base64pdf

    fun tilleggsopplysning() = listOf(Tilleggsopplysning("isSensitiv", letterMetadata.isSensitiv.toString()))
}


data class LetterMetadata(val displayTitle: String, val isSensitiv: Boolean)