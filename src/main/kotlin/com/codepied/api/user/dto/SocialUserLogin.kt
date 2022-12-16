package com.codepied.api.user.dto

import javax.validation.constraints.NotBlank

data class SocialUserLogin(
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val authorizationCode: String
)