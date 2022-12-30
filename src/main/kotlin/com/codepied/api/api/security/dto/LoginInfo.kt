package com.codepied.api.api.security.dto

/**
 * Login Response interface
 *
 * @author Aivyss
 * @since 2022/12/17
 */
interface LoginInfo {
    fun getUserKey(): Long
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun getNickname(): String
    fun getUserProfile(): String?
    fun getEmail(): String?
}