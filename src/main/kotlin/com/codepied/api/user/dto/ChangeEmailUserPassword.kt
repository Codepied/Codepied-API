package com.codepied.api.user.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


class ChangeEmailUserPassword(
    /**
     * 8자리 이상 15자리 이하 특문포함.
     */
    @field:Pattern(message = "EXCEPTION.NOT_EXACT_PASSWORD_PATTERN", regexp="^.*(?=^.{8,15}\$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#\$%^&+=]).*\$")
    val newPassword: String,
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    val oldPassword: String,
)