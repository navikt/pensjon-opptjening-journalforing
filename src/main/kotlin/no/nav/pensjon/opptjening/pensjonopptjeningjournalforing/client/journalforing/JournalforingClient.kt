package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util.toJson
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class JournalforingClient(@Qualifier("journalforingRestTemplate") private val restTemplate: RestTemplate, @Value("\${JOURNALFORING_URL}") private val url: String) {

    private val urlWithParams = UriComponentsBuilder
        .fromUriString(url)
        .queryParam("forsoekFerdigstill", "true")
        .build()
        .toUri()

    fun opprettJournalpost(request: OpprettJournalpostRequest): OpprettJournalpostResponse {
        return restTemplate.exchange(
            urlWithParams,
            HttpMethod.POST,
            HttpEntity(request.toJson(), applicationJsonHeader),
            OpprettJournalpostResponse::class.java
        ).body!!
    }

    companion object {
        private val applicationJsonHeader = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
    }
}