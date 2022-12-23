package com.codepied.api.api.externalApi

import com.codepied.api.api.security.SocialAccount
import com.codepied.api.api.security.SocialType
import org.springframework.stereotype.Service

@Service
class NaverLoginApiService : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        TODO("Not yet implemented")
    }

    override fun supportType(): SocialType = SocialType.NAVER
}