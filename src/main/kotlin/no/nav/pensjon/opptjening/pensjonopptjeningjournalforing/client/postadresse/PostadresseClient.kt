package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.postadresse

import org.springframework.web.client.RestTemplate

class PostadresseClient(private val restTemplate: RestTemplate, private val url: String) {

    internal fun hentPostadresse(fnr: String) {
        //TODO implementer rest kall
    }
}