package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.dokdistfordeling

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo
import com.github.tomakehurst.wiremock.junit5.WireMockTest
import no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.MockTokenConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate

@SpringBootTest
@WireMockTest
internal class DokdistfordelingClientConfigTest {

    @Qualifier("dokdistfordelingRestTemplate")
    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Test
    fun `dokdistfordelingRestTemplate should call endpoint with dokdistfordeling token`(wiremockServer: WireMockRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/")
                .willReturn(WireMock.ok())
        )

        restTemplate.getForEntity(wiremockServer.httpBaseUrl, String::class.java)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.DOKDISTFORDELING_TOKEN}"))
        )
    }
}