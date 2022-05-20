package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*

class KrrClient(
    private val restTemplate: RestTemplate,
    private val url: String,
    registry: MeterRegistry,
) {

    private val counterFailedKrr = registry.counter("krr_request", "status", "failed")
    private val counterSuccessfulKrr = registry.counter("krr_request", "status", "ok")
    private val counterNotFoundKrr = registry.counter("krr_request", "status", "notfound")

    fun getLanguageCode(fnr: String): LanguageCode {
        try {
            val response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity("", createHeaders(fnr)),
                KrrResponse::class.java
            )
            counterSuccessfulKrr.increment()
            return response.body?.getLanguageCode() ?: LanguageCode.standardLanguage()

        } catch (e: HttpStatusCodeException) {
            if (e.statusCode == HttpStatus.NOT_FOUND) {
                counterNotFoundKrr.increment()
                return LanguageCode.standardLanguage()
            } else {
                logger.error("En feil oppstod ved kall mot KRR: ${e.statusCode.value()} ${e.responseBodyAsString}")
                println("En feil oppstod ved kall mot KRR: ${e.statusCode.value()} ${e.responseBodyAsString}")
                counterFailedKrr.increment()
                throw e
            }
        }
    }

    private fun createHeaders(fnr: String) = HttpHeaders().apply {
        contentType = MediaType.APPLICATION_JSON
        add("Nav-Personident", fnr)
        add("Nav-Call-Id", UUID.randomUUID().toString())
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KrrClient::class.java)
    }
}