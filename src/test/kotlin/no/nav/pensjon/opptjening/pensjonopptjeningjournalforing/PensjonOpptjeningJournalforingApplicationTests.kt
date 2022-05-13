package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pgi-endring-topic"])
class PensjonOpptjeningJournalforingApplicationTests {

    @Test
    fun contextLoads() {
    }

}
