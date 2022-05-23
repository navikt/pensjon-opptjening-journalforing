package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.krr

import io.micrometer.core.instrument.MeterRegistry
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.brevbaking.model.LanguageCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*


@Component
class KrrClient(
    @Qualifier("krrRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${KRR_PROXY_URL}") private val url: String,
    registry: MeterRegistry,
) {

    private val counterFailedKrr = registry.counter("krr_request", "status", "failed")
    private val counterSuccessfulKrr = registry.counter("krr_request", "status", "ok")
    private val counterNotFoundKrr = registry.counter("krr_request", "status", "notfound")


    @Retryable(
        include = [HttpStatusCodeException::class],
        backoff = Backoff(delay = 1000L, maxDelay = 140000L, multiplier = 3.0)
    )
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