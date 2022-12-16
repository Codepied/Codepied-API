package com.codepied.api.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

/**
 * Email user login request
 *
 * @author Aivyss
 * @since 2022/12/17
 */
data class EmailUserLogin(
    @field:Email(message = "EXCEPTION.PARAMETERS.EMAIL_FORMAT")
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val email: String,

    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val password: String,
)