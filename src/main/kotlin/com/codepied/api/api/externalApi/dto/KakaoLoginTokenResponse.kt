package com.codepied.api.api.externalApi.dto

class KakaoLoginTokenResponse(
    val token_type: String?,
    val access_token: String,
    val id_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
)