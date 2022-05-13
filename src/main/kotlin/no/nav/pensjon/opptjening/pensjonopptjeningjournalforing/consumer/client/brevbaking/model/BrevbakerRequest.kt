package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevbaking.model

interface BrevbakerRequest {
    fun toJson(): String
}