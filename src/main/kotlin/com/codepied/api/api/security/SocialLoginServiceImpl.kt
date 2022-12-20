package com.codepied.api.api.security

import com.codepied.api.api.externalApi.KakaoLoginApiService
import com.codepied.api.api.externalApi.SpecificProviderLoginApiService
import com.codepied.api.api.security.dto.SociaAuthlApi
import com.codepied.api.domain.User
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SocialLoginServiceImpl (private val webClient: WebClient, private val KakaoLoginApiService: SpecificProviderLoginApiService): SocialLoginService {
    override fun signup(socialType: SocialType, authorizationCode: String): LoginInfo {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }

    override fun login(socialType: SocialType, authorizationCode: String): LoginInfo {


        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {
                println("kakao login logic start");
                KakaoLoginApiService.login(authorizationCode);
            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }



    override fun logout(socialType: SocialType, authorizationCode: String, user: User) {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }
}