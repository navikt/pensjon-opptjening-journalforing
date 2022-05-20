package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

class KrrClient(
    private val restTemplate: RestTemplate,
    private val url: String,
    registry: MeterRegistry
    ) {

    private val counterFailedKrr = registry.counter("krr_request", "status", "failed")
    private val counterSuccessfulKrr = registry.counter("krr_request", "status", "ok")

    fun getLanguageCode(fnr: String): LanguageCode {
        try {
            val response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity("", createHeaders(fnr)),
                KrrResponse::class.java
            )
            counterSuccessfulKrr.increment()
            return response.body!!.getLanguageCode()

        } catch (e: HttpStatusCodeException) {
            logger.error("En feil oppstod ved kall mot KRR: ${e.statusCode.value()} ${e.responseBodyAsString}")
            counterFailedKrr.increment()
            throw e
        }
    }

    private fun createHeaders(fnr: String) = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        add("Nav-Personident", fnr)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KrrClient::class.java)
    }
}