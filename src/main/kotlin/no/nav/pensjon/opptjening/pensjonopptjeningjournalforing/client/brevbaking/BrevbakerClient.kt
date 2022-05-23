package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.service.BrevKode
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LetterMetadata
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevbakingRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.BrevbakingResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class BrevbakerClient(@Qualifier("brevbakerRestTemplate") private val restTemplate: RestTemplate, @Value("\${BREVBAKER_URL}") private val url: String) {

    internal fun lagBrev(brevKode: BrevKode, request: BrevbakingRequest) : BrevbakingResponse{
        return BrevbakingResponse(base64pdf = "", letterMetadata = LetterMetadata(displayTitle = "", isSensitiv = false))
    }
}