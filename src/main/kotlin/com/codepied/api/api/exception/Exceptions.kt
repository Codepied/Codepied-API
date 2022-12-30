package com.codepied.api.api.exception

import com.codepied.api.api.CodeEnum
import org.springframework.core.NestedRuntimeException
import org.springframework.http.HttpStatus

/**
 * customized exceptions
 *
 * @author Aivyss
 * @since 26/12/2022
 */
sealed class CodepiedBaseException(
    val errorCode: CodeEnum = BusinessErrorCode.UNKNOWN_ERROR,
    val messageArgs: Array<String>,
    val debugMessage: String,
    val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
): NestedRuntimeException(debugMessage) {
    /**
     * exception for invalid data processing (HttpStatus = 4xx)
     *
     * @author Aivyss
     * @since 11/30/2022
     */
    class InvalidRequestException(
        errorCode: BusinessErrorCode = BusinessErrorCode.UNKNOWN_ERROR,
        messageArgs: Array<String>,
        debugMessage: String,
        httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    ): CodepiedBaseException(errorCode, messageArgs, debugMessage, httpStatus)

    /**
     * exception for Server Error (HttpStatus = 5xx)
     *
     * @author Aivyss
     * @since 12/26/2022
     */
    class ServerException(
        errorCode: ServerErrorCode = ServerErrorCode.INTERNAL_SERVER_ERROR,
        messageArgs: Array<String> = emptyArray(),
        debugMessage: String,
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    ): CodepiedBaseException(errorCode, messageArgs, debugMessage, httpStatus)
}