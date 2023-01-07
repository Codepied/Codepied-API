package com.codepied.api.user.dto

import javax.validation.constraints.NotBlank

/**
 * request body for refresh tokens
 *
 * @author Aivyss
 * @since 2023/01/07
 */
data class RefreshTokens(
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val refresh: String
)