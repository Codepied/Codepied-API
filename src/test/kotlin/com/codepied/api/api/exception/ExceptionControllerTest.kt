package com.codepied.api.api.exception

import com.codepied.api.api.exception.ServerExceptionBuilder.serverError
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.http.SupportLanguage
import com.codepied.api.test.AbstractServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.anyArray
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.context.MessageSource

class ExceptionControllerTest : AbstractServiceTest() {
    @Mock
    private lateinit var messageSource: MessageSource
    @Mock
    private lateinit var requestContext: RequestContext

    @InjectMocks
    private lateinit var exceptionController: ExceptionController

    @Test
    fun `success to handle internal server error`() {
        val localeMessage = "test message"
        val exception = serverError(debugMessage = "test exception")
        doReturn(SupportLanguage.KO).`when`(requestContext).supportLanguage
        doReturn(localeMessage).`when`(messageSource).getMessage(anyString(), anyArray(), eq(SupportLanguage.KO.locale))

        val failResponse = exceptionController.internalServerError(exception)
        assertThat(failResponse.errorCode).isEqualTo(exception.errorCode.getNameValue())
        assertThat(failResponse.interfaceMessage).isEqualTo(localeMessage)
    }
}