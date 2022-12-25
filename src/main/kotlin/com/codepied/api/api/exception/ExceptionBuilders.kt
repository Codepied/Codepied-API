package com.codepied.api.api.exception

import com.codepied.api.api.CodeEnum
import org.springframework.http.HttpStatus

object InvalidRequestExceptionBuilder {
    fun throwInvalidRequest(
        errorCode: CodeEnum,
        debugMessage: String,
        messageArgs: Array<String> = emptyArray(),
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    ): Nothing {
        throw InvalidRequestException(
            errorCode = errorCode,
            messageArgs = messageArgs,
            debugMessage = debugMessage,
            httpStatus = httpStatus,
        )
    }

    fun invalidRequest(
        errorCode: CodeEnum,
        debugMessage: String,
        messageArgs: Array<String> = emptyArray(),
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    ): InvalidRequestException = InvalidRequestException(
        errorCode = errorCode,
        messageArgs = messageArgs,
        debugMessage = debugMessage,
        httpStatus = httpStatus,
    )

    fun throwInternalServerError(): Nothing = throwInvalidRequest(
        errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
        debugMessage = "unknown server error",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    )

    fun throwEnumCodeError(): Nothing = throwInvalidRequest(
        errorCode = ErrorCode.INVALID_ENUM_CODE_ERROR,
        debugMessage = "invalid enum input",
        httpStatus = HttpStatus.BAD_REQUEST
    )

    fun throwNoSuchUser(): Nothing = throwInvalidRequest(
        errorCode = ErrorCode.NO_SUCH_USER_LOGIN,
        debugMessage = "no such user",
        httpStatus = HttpStatus.BAD_REQUEST
    )

    fun throwInvalidPassword() : Nothing = throwInvalidRequest(
        errorCode = ErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR,
        debugMessage = "not accessible user",
        httpStatus = HttpStatus.BAD_REQUEST,
    )
}