package com.codepied.api.api.exception

import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.http.FailResponse
import com.codepied.api.api.http.FailResponseFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.*

/**
 * general exception handler
 *
 * @author Aivyss
 * @since 11/30/2022
 */
@RestControllerAdvice
class ExceptionController(
    private val messageSource: MessageSource,
    private val requestContext: RequestContext,
) {
    private val log: Logger = LoggerFactory.getLogger(ExceptionController::class.java)

    /**
     * exception handling: exception for invalid data processing
     */
    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun invalidRequestException(e: InvalidRequestException): FailResponse {
        log.info(e.debugMessage)

        val localeMessage = getMessage(e.errorCode.getCodeValue(), e.messageArgs, requestContext.supportLanguage.locale)

        return FailResponseFactory.create(
            e = e,
            localeMessage = localeMessage,
            supportLanguage = requestContext.supportLanguage,
            httpStatus = e.httpStatus,
            now = LocalDateTime.now()
        )
    }

    /**
     * exception handling: validation fail exception for validation of request body
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgsNotValidException(e: MethodArgumentNotValidException): FailResponse {
        val fieldErrors = e.bindingResult.fieldErrors

        val errorCode = if (fieldErrors.isNotEmpty()) {
            ParameterErrorCode.matches(e.fieldErrors[0].defaultMessage)
        } else {
            ErrorCode.INTERNAL_SERVER_ERROR
        }

        val localeMessage = getMessage(errorCode.getCodeValue(), emptyArray(), requestContext.supportLanguage.locale)

        return FailResponseFactory.create(
            errorCode = errorCode.getNameValue(),
            localeMessage = localeMessage,
            supportLanguage = requestContext.supportLanguage,
            httpStatus = HttpStatus.BAD_REQUEST,
            now = LocalDateTime.now(),
        )
    }

    private fun getMessage(codeValue: String, args: Array<String>?, locale: Locale): String {
        return try {
            messageSource.getMessage(codeValue, args, locale)
        } catch (e: NoSuchMessageException) {
            messageSource.getMessage(
                ErrorCode.INTERNAL_SERVER_ERROR.getCodeValue(),
                emptyArray(),
                requestContext.supportLanguage.locale
            )
        }
    }
}