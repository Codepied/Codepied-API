package com.codepied.api.api.exception

import com.codepied.api.api.CodeEnum
import com.codepied.api.api.exception.ErrorCode
import org.springframework.core.NestedRuntimeException
import org.springframework.http.HttpStatus

/**
 * exception for invalid data processing
 *
 * @author Aivyss
 * @since 11/30/2022
 */
class InvalidRequestException(
    val errorCode: CodeEnum = ErrorCode.INTERNAL_SERVER_ERROR,
    val messageArgs: Array<String>,
    val debugMessage: String,
    val httpStatus: HttpStatus =  HttpStatus.BAD_REQUEST,
): NestedRuntimeException(debugMessage)