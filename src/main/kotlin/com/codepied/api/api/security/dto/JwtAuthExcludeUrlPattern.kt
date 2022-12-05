package com.codepied.api.api.security.dto

/**
 * JqwtAuthExcludeUrlPatterns
 *
 * @author Aivyss
 * @since 12/03/2022
 */
enum class JwtAuthExcludeUrlPattern(val url: String) {
    USER_LOGIN("/api/users/auth/login"),
    USER_SIGNUP("/api/users/auth/signup"),
    ;
}