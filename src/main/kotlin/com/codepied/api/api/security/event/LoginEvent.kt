package com.codepied.api.api.security.event

class LoginEvent(
    val userId: Long,
    val status: LoginStatus = LoginStatus.SUCCESS
)

enum class LoginStatus {
    SUCCESS,
    PASSWORD_FAIL,
    EMAIL_AUTHORIZATION_FAIL,
    UNKNOWN_FAIL,
    ;
}