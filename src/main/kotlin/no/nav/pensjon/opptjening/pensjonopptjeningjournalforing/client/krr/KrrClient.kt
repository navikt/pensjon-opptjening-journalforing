package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class KrrClient(
    private val restTemplate: RestTemplate,
    private val url: String,
) {

    fun hentSpraak(fnr: String): LanguageCode {
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity("", createHeaders(fnr)),
            KrrResponse::class.java
        )

        return response.body!!.getLanguageCode()
    }


    private fun createHeaders(fnr: String) = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        add("Nav-Personident", fnr)
    }
}