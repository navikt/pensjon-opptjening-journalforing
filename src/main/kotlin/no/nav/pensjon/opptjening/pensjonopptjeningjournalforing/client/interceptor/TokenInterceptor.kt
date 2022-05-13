package no.nav.pensjon.opptjening.pensjonopptjeningjournalforing.client.interceptor

import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import pensjon.opptjening.azure.ad.client.TokenProvider

class TokenInterceptor(private val tokenProvider: TokenProvider) : ClientHttpRequestInterceptor {
    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        request.headers.setBearerAuth(tokenProvider.getToken())
        return execution.execute(request, body)
    }
}