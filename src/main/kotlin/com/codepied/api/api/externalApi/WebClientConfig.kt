package com.codepied.api.api.externalApi

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.Charset

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .defaultHeaders {
                it.acceptCharset = listOf(Charset.forName("UTF-8"))
                it.contentType = MediaType.APPLICATION_JSON
            }.codecs {
                it.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
            }
            .build()
    }
}