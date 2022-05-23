package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.dokdistfordeling

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class DokDistClient(
    @Qualifier("dokdistRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${DOKDISTFORDELING_URL}") private val url: String,
) {

    fun distribuerBrev(request: DokDistRequest): String {
        //TODO håndter response
        val response: ResponseEntity<String> = restTemplate.postForEntity(url, request, String::class.java)
        return response.body!!
    }
}