package com.codepied.api.api.security.dto

data class KakaoAuthResult(
        val access_token: String?,
        val token_type: String?,
        val refresh_token: String?,
        val expires_in: Int?,
        val scope: String?,
        val refresh_token_expires_in: Int?,
        val error: String?,
        val error_description: String?,
        val error_code: String?,
        ) {

}

