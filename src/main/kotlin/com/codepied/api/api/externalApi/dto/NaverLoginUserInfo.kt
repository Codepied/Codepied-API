package com.codepied.api.api.externalApi.dto

import com.codepied.api.api.security.dto.SocialAccount

class NaverLoginUserInfo(
    val resultCode: String?,
    val message: String?,
    val response: NaverLoginUserData?,
) {
    fun create(): SocialAccount? {
        return if (response != null) SocialAccountImpl(
            id = response.id,
            email = response.email,
        ) else null
    }
}