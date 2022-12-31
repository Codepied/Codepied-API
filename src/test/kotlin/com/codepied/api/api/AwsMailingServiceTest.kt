package com.codepied.api.api

import com.codepied.api.api.mailing.application.AwsMailingService
import com.codepied.api.api.mailing.domain.EmailTemplate
import com.codepied.api.api.mailing.domain.EmailTemplateRepository
import com.codepied.api.api.mailing.domain.EmailTemplateType
import com.codepied.api.api.mailing.event.SignupEmailAuthorizationEvent
import com.codepied.api.test.AbstractServiceTest
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.SendEmailRequest
import java.util.concurrent.CompletableFuture

class AwsMailingServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var client: SesAsyncClient
    @Mock
    private lateinit var emailTemplateRepository: EmailTemplateRepository

    @InjectMocks
    private lateinit var service: AwsMailingService

    @Test
    fun `이메일 회원가입 인증 메일 발송 성공`() {
        // * given
        val event = SignupEmailAuthorizationEvent(
            to = "test@test.com",
            uri = "https://codepied.com/auths/signup"
        )
        val template = EmailTemplate(
            id = 1L,
            title = "title",
            template = "<div> template to={{to}} uri={{uri}} </div>",
            type = EmailTemplateType.EMAIL_SIGNUP_AUTHORIZATION
        )
        doReturn(template).`when`(emailTemplateRepository).getByType(eq(EmailTemplateType.EMAIL_SIGNUP_AUTHORIZATION))
        doReturn(CompletableFuture.completedFuture(null)).`when`(client).sendEmail(any<SendEmailRequest>())

        // * when
        service.sendSignupAuthorizationEmail(event)
    }
}