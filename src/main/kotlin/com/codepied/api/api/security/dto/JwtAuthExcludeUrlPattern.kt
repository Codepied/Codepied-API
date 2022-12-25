package com.codepied.api.api.security.dto

/**
 * JqwtAuthExcludeUrlPatterns
 *
 * @author Aivyss
 * @since 12/03/2022
 */
enum class JwtAuthExcludeUrlPattern(val url: String) {
    USER_LOGIN("/api/users/auths/login"),
    USER_SIGNUP("/api/users/auths/signup"),
    USER_INFO_DUPLICATE("/api/users/info/duplicate")
    ;
}