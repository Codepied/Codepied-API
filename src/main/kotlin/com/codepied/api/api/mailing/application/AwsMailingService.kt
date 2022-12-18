package com.codepied.api.api.mailing.application

import com.codepied.api.api.mailing.domain.EmailTemplate
import com.codepied.api.api.mailing.domain.EmailTemplateRepository
import com.codepied.api.api.mailing.domain.EmailTemplateType
import com.codepied.api.api.mailing.domain.getByType
import com.codepied.api.api.mailing.dto.EmailSignupAuthorizationValues
import com.codepied.api.api.mailing.dto.EmailTemplateValues
import com.codepied.api.api.mailing.dto.content
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.ses.SesAsyncClient
import software.amazon.awssdk.services.ses.model.*

@Service
class AwsMailingService(
    private val client: SesAsyncClient,
    private val emailTemplateRepository: EmailTemplateRepository,
) {
    private val from: String = "noreply@codepied.com"

    @Async
    fun sendSignupAuthorizationEmail(values: EmailSignupAuthorizationValues) {
        val template = emailTemplateRepository.getByType(EmailTemplateType.EMAIL_SIGNUP_AUTHORIZATION)

        sendEmail(values, template)
    }

    private fun sendEmail(values: EmailTemplateValues, template: EmailTemplate) {
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