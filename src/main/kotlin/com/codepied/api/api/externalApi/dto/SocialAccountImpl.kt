package com.codepied.api.api.externalApi.dto

import com.codepied.api.api.security.SocialAccount

class SocialAccountImpl(
    private val id: String,
    private val email: String,
) : SocialAccount {
    override fun socialIdentification(): String = id

    override fun email(): String = email
}