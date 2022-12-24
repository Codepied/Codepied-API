package com.codepied.api.api.externalApi.dto

class NaverLoginTokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String,
    val expires_in: Int,
    val error: String?,
    val error_description: String?,
)