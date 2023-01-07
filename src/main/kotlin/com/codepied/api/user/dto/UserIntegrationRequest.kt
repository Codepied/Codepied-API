package com.codepied.api.user.dto

import com.codepied.api.api.security.SocialType
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SocialUserIntegrationRequest(
    @field:NotNull(message = "EXCEPTION.PARAMETERS.NOT_NULL")
    val socialType: SocialType,
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val authorizationCode: String,
)