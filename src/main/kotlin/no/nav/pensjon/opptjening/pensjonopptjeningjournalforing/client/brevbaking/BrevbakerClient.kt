package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterMetadata
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterResponse
import org.springframework.web.client.RestTemplate

class BrevbakerClient(private val restTemplate: RestTemplate, private val url: String) {

    internal fun lagBrev(brevKode: BrevKode, request: LetterRequest) : LetterResponse{
        return LetterResponse(base64pdf = "", letterMetadata = LetterMetadata(displayTitle = "", isSensitiv = false))
    }
}