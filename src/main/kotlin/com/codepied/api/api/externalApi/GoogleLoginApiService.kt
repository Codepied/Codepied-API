package com.codepied.api.api.externalApi

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.externalApi.dto.GoogleLoginData
import com.codepied.api.api.security.dto.SocialAccount
import com.codepied.api.api.security.SocialType
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

/**
 * Google Social Auth Service
 * @author Aivyss
 * @since 2022/12/24
 */
@Service
class GoogleLoginApiService(private val webClient: WebClient) : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        val loginData = webClient.get()
            .uri("https://www.googleapis.com/oauth2/v2/userinfo?access_token=$authorizationCode")
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono {
                it.bodyToMono(object : ParameterizedTypeReference<GoogleLoginData>() {})
            }.block()

        return loginData?.create() ?: throw invalidRequest(
            errorCode = ErrorCode.NOT_ACCESSIBLE_SOCIAL_USER_GOOGLE,
            debugMessage = "maybe does not google user.",
            httpStatus = HttpStatus.BAD_REQUEST)
    }

    override fun supportType(): SocialType = SocialType.GOOGLE
}