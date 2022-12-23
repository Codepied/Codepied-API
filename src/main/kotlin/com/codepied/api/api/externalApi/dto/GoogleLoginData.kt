package com.codepied.api.api.externalApi.dto

import com.codepied.api.api.security.SocialAccount

class GoogleLoginData(
    val id: String,
    val email: String,
) {
    fun create(): SocialAccount {
        return SocialAccountImpl(
            id = id,
            email = email,
        )
    }
}