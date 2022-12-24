package com.codepied.api.api.externalApi.dto

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder
import com.codepied.api.api.security.dto.SocialAccount

class KakaoLoginUserInfo(
    val id: Long,
    val kakao_account: KakaoLoginUserData?
) {
    fun create(): SocialAccount {
        val invalidRequest = InvalidRequestExceptionBuilder.invalidRequest(
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