package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper




fun Any.toJson() : String = jacksonObjectMapper()
    .registerModule(JavaTimeModule())
    .writerWithDefaultPrettyPrinter()
    .writeValueAsString(this)

fun Any.toJson(nonempty: Boolean = false): String {
    return if (nonempty) {
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(this)
    } else {
        this.toJson()
    }
}