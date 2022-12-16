package com.codepied.api.api.security

import com.codepied.api.domain.User

interface SocialLoginService {
    fun signup(socialType: SocialType, authorizationCode: String): LoginInfo
    fun login(socialType: SocialType, authorizationCode: String): LoginInfo
    fun logout(socialType: SocialType, authorizationCode: String, user: User)
}

interface LoginInfo {
    fun getUser(): User
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun getNickname(): String
    fun getUserProfile(): String
    fun getEmail(): String
}