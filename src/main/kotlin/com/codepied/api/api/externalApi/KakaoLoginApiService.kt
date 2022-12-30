package com.codepied.api.api.externalApi

import com.codepied.api.api.config.KakaoSocialLoginProperty
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.externalApi.dto.KakaoLoginTokenResponse
import com.codepied.api.api.externalApi.dto.KakaoLoginUserInfo
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.SocialAccount
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

/**
 * social login Api Service: provider: KAKAO
 *
 * @author Aivyss
 * @since 2022/12/25
 */
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
            errorCode = BusinessErrorCode.NOT_ACCESSIBLE_SOCIAL_USER_NAVER,
            debugMessage = "maybe does not google user.",
            httpStatus = HttpStatus.BAD_REQUEST
        )
    }

    override fun supportType(): SocialType = SocialType.KAKAO
}
