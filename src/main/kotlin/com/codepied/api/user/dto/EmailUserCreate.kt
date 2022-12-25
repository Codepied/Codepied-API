package com.codepied.api.endpoint.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * test dto
 *
 * @author Aivyss
 * @since 11/30/2022
 */
data class EmailUserCreate(
    @field:Email(message = "EXCEPTION.PARAMETERS.EMAIL_FORMAT")
    val email: String,

    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val password: String,

    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    @field:Size(min= 2, max = 15, message = "EXCEPTION.PARAMETERS.NICKNAME_SIZE")
    val nickname: String,
)