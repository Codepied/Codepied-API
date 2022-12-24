package com.codepied.api.api.externalApi

import com.codepied.api.api.security.dto.SocialAccount
import com.codepied.api.api.security.SocialType
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class SocialLoginApiServiceImpl(applicationContext: ApplicationContext) : SocialLoginApiService {
    private val apiService = applicationContext
        .getBeansOfType(SpecificProviderLoginApiService::class.java)
        .values.associateBy { it.supportType() }

    override fun loginAuthorization(socialType: SocialType, authorizationCode: String): SocialAccount {
        val socialAccount = apiService[socialType]?.login(authorizationCode)
        TODO("Not yet implemented")
    }
}