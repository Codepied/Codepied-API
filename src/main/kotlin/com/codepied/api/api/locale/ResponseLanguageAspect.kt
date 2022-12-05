package com.codepied.api.api.locale

import com.codepied.api.api.http.BaseResponse
import com.codepied.api.api.http.RequestContext
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@Aspect
@Component
@Order(1)
@RestController
@RestControllerAdvice
class ResponseLanguageAspect(
    private val requestContext: RequestContext,
) {

    @Around("@within(org.springframework.web.bind.annotation.RestController)" + "||" +
            "@within(org.springframework.web.bind.annotation.RestControllerAdvice)")
    fun point(point: ProceedingJoinPoint): Any? {
        return point.proceed().apply {
            if (this is BaseResponse) {
                this.supportLanguage = requestContext.supportLanguage
            }
        }
    }
}