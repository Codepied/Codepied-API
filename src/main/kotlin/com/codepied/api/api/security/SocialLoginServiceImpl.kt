package com.codepied.api.api.security

import com.codepied.api.api.security.dto.SampleDto
import com.codepied.api.domain.User
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class SocialLoginServiceImpl (private val webClient: WebClient): SocialLoginService {
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
        val result = webClient.post().uri("https://reqbin.com/echo/post/json").exchangeToMono {
            it.bodyToMono(object : ParameterizedTypeReference<SampleDto>() {
            })
        }.block()

        println(result?.success);

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