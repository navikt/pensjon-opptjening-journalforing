package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class PensjonOpptjeningJournalforingApplication

fun main(args: Array<String>) {
    runApplication<PensjonOpptjeningJournalforingApplication>(*args)
}
