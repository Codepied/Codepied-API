package com.codepied.api.api.security

import com.codepied.api.api.CodeEnum
import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import org.springframework.http.HttpStatus

enum class SocialType: CodeEnum {
    EMAIL,
    KAKAO,
    NAVER,
    GOOGLE,
    ;

    companion object {
        fun matches(code: String?): SocialType {
            return SocialType.values().find { it.name == code } ?: throwInvalidRequest(
                errorCode = ErrorCode.NO_RESOURCE_ERROR,
                debugMessage = "no social login type",
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }
    }

    override fun getCodeValue(): String = "EXCEPTION.PARAMETERS.${name}"
    override fun getNameValue(): String = this.name
}