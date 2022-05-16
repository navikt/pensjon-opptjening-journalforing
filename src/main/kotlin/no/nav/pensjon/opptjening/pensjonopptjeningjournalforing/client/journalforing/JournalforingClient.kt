package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.journalforing

import org.springframework.web.client.RestTemplate

class JournalforingClient(private val restTemplate: RestTemplate, private val url: String) {

    fun opprettJournalpost(){
        //TODO implementer rest kall
    }
}