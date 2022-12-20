package com.codepied.api.api.externalApi

import com.codepied.api.api.security.SocialAccount
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
@Primary
@Service
class KakaoLoginApiService (private val kakaoAuthTokenApiService: KakaoAuthTokenApiService): SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        val authTokenResult = kakaoAuthTokenApiService.result(authorizationCode);
        println("authTokenResult:: $authTokenResult");

        TODO("Not yet implemented")
    }
}