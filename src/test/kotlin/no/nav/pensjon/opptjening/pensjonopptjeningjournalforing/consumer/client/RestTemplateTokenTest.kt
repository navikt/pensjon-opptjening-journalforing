package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.client.RestTemplate

@SpringBootTest
@WireMockTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = ["pensjonopptjening.pgi-endring-topic"])
class RestTemplateTokenTest {


    @Qualifier("journalforingRestTemplate")
    @Autowired
    private lateinit var journalforingRestTemplate: RestTemplate

    @Qualifier("brevbakerRestTemplate")
    @Autowired
    private lateinit var brevbakerRestTemplate: RestTemplate

    @Qualifier("dokdistRestTemplate")
    @Autowired
    private lateinit var dokdistfordelingRestTemplate: RestTemplate

    @Test
    fun `journalforingRestTemplate should call endpoint with journalforing token`(wiremockServer: WireMockRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/")
                .willReturn(WireMock.ok())
        )

        journalforingRestTemplate.getForEntity(wiremockServer.httpBaseUrl, String::class.java)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.JOURNALFORING_TOKEN}"))
        )
    }


    @Test
    fun `brevbakingRestTemplate should call endpoint with brevbaking token`(wiremockServer: WireMockRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/")
                .willReturn(WireMock.ok())
        )

        brevbakerRestTemplate.getForEntity(wiremockServer.httpBaseUrl, String::class.java)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.BREVBAKING_TOKEN}"))
        )
    }

    @Test
    fun `dokdistfordelingRestTemplate should call endpoint with dokdistfordeling token`(wiremockServer: WireMockRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/")
                .willReturn(WireMock.ok())
        )

        dokdistfordelingRestTemplate.getForEntity(wiremockServer.httpBaseUrl, String::class.java)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.DOKDISTFORDELING_TOKEN}"))
        )
    }
}