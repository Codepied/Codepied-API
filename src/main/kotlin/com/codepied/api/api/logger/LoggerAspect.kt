package com.codepied.api.api.logger

import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime

/**
 * HttpRequest Logger
 *
 * @author Aivyss
 * @since 12/01/2022
 */
@Aspect
@Component
@Order(10)
class LoggerAspect(
    private val objectMapper: ObjectMapper,
) {
    val log: Logger = LoggerFactory.getLogger(LoggerAspect::class.java)

    @Around("@within(com.codepied.api.api.logger.Log)")
    fun point(point: ProceedingJoinPoint): Any {
        return if (point.signature is MethodSignature) {
            val signature: MethodSignature = point.signature as MethodSignature
            val parameters = signature.method.parameters
            val arguments = point.args

            var hasRequestBody = false
            for (idx in parameters.indices) {
                val requestBodyAnno = parameters[idx].getAnnotation(RequestBody::class.java)
                hasRequestBody = requestBodyAnno != null

                if (hasRequestBody) {
                    val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
                    val request = requestAttributes.request
                    val method = request.method
                    val url = request.requestURI
                    val requestBody = objectMapper.writeValueAsString(arguments[idx])

                    val logMessage = RequestLogDto(
                        method = method,
                        url = url,
                        requestBody = requestBody,
                        requestTime = LocalDateTime.now(),
                    )

                    log.info(objectMapper.writeValueAsString(logMessage))
                }
            }
            point.proceed()
        } else {
            point.proceed()
        }
    }
}

data class RequestLogDto(
    val method: String,
    val url: String,
    val requestBody: String,
    val requestTime: LocalDateTime,
)