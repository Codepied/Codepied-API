package com.codepied.api.api.externalApi

import com.codepied.api.api.security.dto.SocialAccount
import com.codepied.api.api.security.SocialType
import org.springframework.stereotype.Service

@Service
class KakaoLoginApiService : SpecificProviderLoginApiService {
    override fun login(authorizationCode: String): SocialAccount {
        TODO("Not yet implemented")
    }

    override fun supportType(): SocialType = SocialType.KAKAO
}