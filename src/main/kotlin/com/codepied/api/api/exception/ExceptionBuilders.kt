package com.codepied.api.api.exception

import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.api.exception.CodepiedBaseException.ServerException
import org.springframework.http.HttpStatus

object InvalidRequestExceptionBuilder {
    fun throwInvalidRequest(
        errorCode: BusinessErrorCode,
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
        errorCode: BusinessErrorCode,
        debugMessage: String,
        messageArgs: Array<String> = emptyArray(),
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    ): InvalidRequestException = InvalidRequestException(
        errorCode = errorCode,
        messageArgs = messageArgs,
        debugMessage = debugMessage,
        httpStatus = httpStatus,
    )

    fun throwUnknownError(): Nothing = throwInvalidRequest(
        errorCode = BusinessErrorCode.UNKNOWN_ERROR,
        debugMessage = "unknown server error",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    )

    fun throwEnumCodeError(): Nothing = throwInvalidRequest(
        errorCode = BusinessErrorCode.INVALID_ENUM_CODE_ERROR,
        debugMessage = "invalid enum input",
        httpStatus = HttpStatus.BAD_REQUEST
    )

    fun throwNoSuchUser(): Nothing = throwInvalidRequest(
        errorCode = BusinessErrorCode.NO_SUCH_USER_LOGIN,
        debugMessage = "no such user",
        httpStatus = HttpStatus.BAD_REQUEST
    )

    fun throwInvalidPassword() : Nothing = throwInvalidRequest(
        errorCode = BusinessErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR,
        debugMessage = "not accessible user",
        httpStatus = HttpStatus.BAD_REQUEST,
    )
}

object ServerExceptionBuilder {
    fun serverError(
        errorCode: ServerErrorCode = ServerErrorCode.INTERNAL_SERVER_ERROR,
        debugMessage: String,
        messageArgs: Array<String> = emptyArray(),
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    ): ServerException {
        return ServerException(
            errorCode = errorCode,
            debugMessage = debugMessage,
            messageArgs = messageArgs,
            httpStatus = httpStatus
        )
    }

    fun throwInternalServerError(): Nothing = throw serverError(debugMessage = "internal server error")
}