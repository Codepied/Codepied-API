package com.codepied.api.api.security.application

import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.LoginInfo
import com.codepied.api.api.security.dto.SocialAccount

/**
 * Social Login Service interface
 *
 * @author Aivyss
 * @since 2022/12/17
 */
interface SocialLoginService {
    fun signup(socialType: SocialType, socialAccount: SocialAccount): LoginInfo
    fun login(socialType: SocialType, authorizationCode: String): LoginInfo
}

