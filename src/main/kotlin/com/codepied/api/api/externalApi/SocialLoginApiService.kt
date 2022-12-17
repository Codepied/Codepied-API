package com.codepied.api.api.externalApi

import com.codepied.api.api.security.LoginInfo
import com.codepied.api.api.security.SocialAccount
import com.codepied.api.api.security.SocialType

interface SocialLoginApiService {
    fun loginAuthorization(socialType: SocialType, authorizationCode: String): SocialAccount
}