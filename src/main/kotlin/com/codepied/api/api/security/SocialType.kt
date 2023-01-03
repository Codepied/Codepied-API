package com.codepied.api.api.security

import com.codepied.api.api.exception.ServerExceptionBuilder.throwCodeError

enum class SocialType {
    EMAIL,
    KAKAO,
    NAVER,
    GOOGLE,
    ;

    companion object {
        fun matches(code: String?): SocialType {
            return SocialType.values().find { it.name == code } ?: throwCodeError()
        }
    }
}