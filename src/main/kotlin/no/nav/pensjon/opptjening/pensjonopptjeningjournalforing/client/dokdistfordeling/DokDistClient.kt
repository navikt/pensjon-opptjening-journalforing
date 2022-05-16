package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class DokDistClient(
    private val url: String,
    private val restTemplate: RestTemplate,
) {

    fun distribuerBrev(request: DokDistRequest): String {
        //TODO håndter response
        val response: ResponseEntity<String> = restTemplate.postForEntity(url, request, String::class.java)
        return response.body!!
    }
}