package com.codepied.api.api.externalApi

import com.codepied.api.api.security.dto.KakaoAuthResult
import com.codepied.api.api.security.dto.SociaAuthlApi
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class KakaoAuthTokenApiService (private val webClient: WebClient){
    fun result (authorizationCode: String): KakaoAuthResult?{
       return webClient
            .post()
            .uri (SociaAuthlApi.KAKAO_AUTH_TOKEN.url){
                it.path(SociaAuthlApi.KAKAO_AUTH_TOKEN.path)
                    .queryParam("response_type", "code")
                    .queryParam("client_id","")
                    .queryParam("redirect_uri","http://localhost:8080")
                    .queryParam("code", authorizationCode)
                    .queryParam("grant_type","authorization_code")
                    .build()
            }
            .exchangeToMono {
                it.bodyToMono(object : ParameterizedTypeReference<KakaoAuthResult>() {})
            }.block();
    }

}