package com.codepied.api.api.security.dto

class EmailLoginInfoImpl(
    private val userKey: Long,
    private val accessToken: String,
    private val refreshToken: String,
    private val nickname: String,
    private val userProfile: String?,
    private val email: String?,
    val passwordChangeRecommended: Boolean,
): LoginInfo {
    override fun getUserKey(): Long = userKey

    override fun getAccessToken(): String = accessToken

    override fun getRefreshToken(): String = refreshToken

    override fun getNickname(): String = nickname

    override fun getUserProfile(): String? = userProfile

    override fun getEmail(): String? = email
}