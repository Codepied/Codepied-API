package com.codepied.api.user.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class ChangeNickname(
    @field:NotBlank(message = "EXCEPTION.PARAMETERS.NOT_BLANK")
    @field:Pattern(message = "EXCEPTION.PARAMETERS.INVALID_NICKNAME", regexp = "^[\\da-zA-Z가-힣]+\$")
    @Size(min = 2, max= 15, message = "EXCEPTION.PARAMETERS.NICKNAME_SIZE")
    val nickname: String,
)