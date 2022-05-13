package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthApi {

    @GetMapping("/ping")
    fun ping(): ResponseEntity<Unit> = ResponseEntity.ok().build()

    @GetMapping("/internal/isalive")
    fun isalive(): ResponseEntity<String> = ResponseEntity.ok("Is alive")

    @GetMapping("/internal/isready")
    fun isready(): ResponseEntity<String> = ResponseEntity.ok().build()
}