package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.consumer.client.brevsending

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
internal class BrevsendingClientConfigTest {

    @Qualifier("brevsendingRestTemplate")
    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Test
    fun `brevsendingRestTemplate should call endpoint with brevsending token`(wiremockServer: WireMockRuntimeInfo) {
        WireMock.stubFor(
            WireMock.get("/")
                .willReturn(WireMock.ok())
        )

        restTemplate.getForEntity(wiremockServer.httpBaseUrl, String::class.java)

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/"))
            .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo("Bearer ${MockTokenConfig.BREVSENDING_TOKEN}"))
        )
    }
}