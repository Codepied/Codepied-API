package com.codepied.api.api.externalApi

import com.codepied.api.api.security.SocialAccount

interface SpecificProviderLoginApiService {
    fun login(authorizationCode: String): SocialAccount
}