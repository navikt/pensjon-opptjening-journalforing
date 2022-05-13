package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking.model.BrevbakerRequest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking.model.BrevbakerResponse
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking.model.BrevKode
import org.springframework.web.client.RestTemplate

class BrevbakerClient(private val restTemplate: RestTemplate, private val url: String) {
    /*
    internal fun lagBrev(brevKode: BrevKode, request: BrevbakerRequest) : BrevbakerResponse {
        return restTemplate.postForEntity(url, request.toJson(), BrevbakerResponse::class.java)
    }

     */

}