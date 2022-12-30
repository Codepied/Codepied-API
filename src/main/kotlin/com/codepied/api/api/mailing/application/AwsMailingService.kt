package com.codepied.api.api.mailing.application

import com.codepied.api.api.mailing.domain.EmailTemplate
import com.codepied.api.api.mailing.domain.EmailTemplateRepository
import com.codepied.api.api.mailing.domain.EmailTemplateType
import com.codepied.api.api.mailing.dto.EmailTemplateEvent
import com.codepied.api.api.mailing.event.SignupEmailAuthorizationEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.*

@Service
class AwsMailingService(
    private val client: SesAsyncClient,
    private val emailTemplateRepository: EmailTemplateRepository,
) {
    private val from: String = "noreply@codepied.com"

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    fun sendSignupAuthorizationEmail(values: SignupEmailAuthorizationEvent) {
        val template = emailTemplateRepository.getByType(EmailTemplateType.EMAIL_SIGNUP_AUTHORIZATION)

        sendEmail(values, template)
    }

    private fun sendEmail(values: EmailTemplateEvent, template: EmailTemplate) {
        val destination = Destination.builder()
            .toAddresses(values.to())
            .build()
        val content = Content.builder().data(values.content(template.template)).build()
        val title = Content.builder().data(template.title).build()
        val body = Body.builder().html(content).build()
        val message = Message.builder().subject(title).body(body).build()

        val emailRequest: SendEmailRequest = SendEmailRequest.builder()
            .destination(destination)
            .message(message)
            .source(from)
            .build()

        client.sendEmail(emailRequest)
    }
}