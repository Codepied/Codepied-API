package com.codepied.api.api.externalApi

import com.codepied.api.api.config.KakaoSocialLoginProperty
import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.externalApi.dto.SocialAccountImpl
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.SocialAccount
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class KakaoLoginApiService(
    private val webClient: WebClient,
    private val property: KakaoSocialLoginProperty,
) : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        // * request token
        val socialAccount = webClient.get()
            .uri(
                with(property) {
                    "$requestTokenUri?" +
                        "grant_type=authorization_code&" +
                        "client_id=$clientId" +
                        "&client_secret=$clientSecret&" +
                        "code=$authorizationCode&" +
                        "redirect_uri=$frontRedirectUri&" +
                        "client_secret=$clientSecret"
                }
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono {
                it.bodyToMono(object : ParameterizedTypeReference<KakaoLoginTokenResponse>() {})
            }.block().let { token ->
                if (token != null) {
                    // * get user info
                    webClient.get()
                        .uri(property.loginUri)
                        .header("Authorization", "Bearer ${token.access_token}")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchangeToMono {
                            it.bodyToMono(object : ParameterizedTypeReference<KakaoLoginUserInfo>() {})
                        }.block()?.create()
                } else null
            }

        return socialAccount ?: throw invalidRequest(
            errorCode = ErrorCode.NOT_ACCESSIBLE_SOCIAL_USER_NAVER,
            debugMessage = "maybe does not google user.",
            httpStatus = HttpStatus.BAD_REQUEST
        )
    }

    override fun supportType(): SocialType = SocialType.KAKAO
}

class KakaoLoginTokenResponse(
    val token_type: String?,
    val access_token: String,
    val id_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
)

class KakaoLoginUserInfo(
    val id: Long,
    val kakao_account: KakaoLoginUserData?
) {
    fun create(): SocialAccount {
        val invalidRequest = invalidRequest(
            errorCode = ErrorCode.NOT_ACCESSIBLE_SOCIAL_USER_KAKAO,
            debugMessage = "not valid email"
        )

        if (kakao_account?.is_email_verified != true) {
            throw invalidRequest
        }

        return SocialAccountImpl(
            id = id.toString(),
            email = kakao_account?.email ?: throw invalidRequest
        )
    }
}

class KakaoLoginUserData(
    val is_email_verified: Boolean?,
    val email: String?
)