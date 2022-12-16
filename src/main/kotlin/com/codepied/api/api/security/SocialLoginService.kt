package com.codepied.api.api.security

import com.codepied.api.domain.User

/**
 * Social Login Service interface
 *
 * @author Aivyss
 * @since 2022/12/17
 */
interface SocialLoginService {
    fun signup(socialType: SocialType, authorizationCode: String): LoginInfo
    fun login(socialType: SocialType, authorizationCode: String): LoginInfo
    fun logout(socialType: SocialType, authorizationCode: String, user: User)
}

/**
 * Login Response interface
 *
 * @author Aivyss
 * @since 2022/12/17
 */
interface LoginInfo {
    fun getUser(): User
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun getNickname(): String
    fun getUserProfile(): String?
    fun getEmail(): String?
}

/**
 * Login Response implementation
 *
 * @author Aivyss
 * @since 2022/12/17
 */
class LoginInfoImpl(
    private val user: User,
    private val accessToken: String,
    private val refreshToken: String,
    private val nickname: String,
    private val userProfile: String?,
    private val email: String?,
): LoginInfo {
    override fun getUser(): User = user

    override fun getAccessToken(): String = accessToken

    override fun getRefreshToken(): String = refreshToken

    override fun getNickname(): String = nickname

    override fun getUserProfile(): String? = userProfile

    override fun getEmail(): String? = email

}