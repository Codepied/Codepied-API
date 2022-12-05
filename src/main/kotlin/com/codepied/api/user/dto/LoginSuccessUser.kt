package com.codepied.api.endpoint.dto

import com.codepied.api.api.role.Role

data class LoginSuccessUser(
    val userKey: Long,
    val accessToken: String,
    val refreshToken: String,
    val roles: List<Role>
)