package com.codepied.api.api.externalApi

import com.codepied.api.api.security.SocialAccount
import org.springframework.stereotype.Service

@Service
class GoogleLoginApiService : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        TODO("Not yet implemented")
    }
}