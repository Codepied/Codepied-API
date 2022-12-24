package com.codepied.api.api.externalApi

import com.codepied.api.api.config.NaverSocialLoginProperty
import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.externalApi.dto.NaverLoginTokenResponse
import com.codepied.api.api.externalApi.dto.NaverLoginUserInfo
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.SocialAccount
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

/**
 * social login Api Service: provider: NAVER
 *
 * @author Aivyss
 * @since 2022/12/24
 */
@Service
class NaverLoginApiService(
    private val webClient: WebClient,
    private val property: NaverSocialLoginProperty,
) : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        // * request token
        val socialAccount = webClient.get()
            .uri(
                with(property) {
                    "$requestTokenUri?grant_type=authorization_code&client_id=$clientId&client_secret=$clientSecret&code=$authorizationCode&state=STATE_STRING"
                }
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono {
                it.bodyToMono(object : ParameterizedTypeReference<NaverLoginTokenResponse>() {})
            }.block().let { token ->
                if (token != null) {
                    // * get user info
                    webClient.get()
                        .uri(property.loginUri)
                        .header("Authorization", "Bearer ${token.access_token}")
                        .accept(MediaType.APPLICATION_JSON)
                        .exchangeToMono {
                            it.bodyToMono(object : ParameterizedTypeReference<NaverLoginUserInfo>() {})
                        }.block()?.create()
                } else null
            }

        return socialAccount ?: throw invalidRequest(
            errorCode = ErrorCode.NOT_ACCESSIBLE_SOCIAL_USER_NAVER,
            debugMessage = "maybe does not google user.",
            httpStatus = HttpStatus.BAD_REQUEST
        )
    }

    override fun supportType(): SocialType = SocialType.NAVER
}
