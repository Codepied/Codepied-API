package com.codepied.api.api.mailing.event

import com.codepied.api.api.config.RunnableEnvProperty
import com.codepied.api.api.mailing.application.AwsMailingService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

/**
 * Send authorization email for email user
 * @author Aivyss
 * @since 2022/12/31
 */
@Component
class SignupEmailAuthorizationEventListener(
    private val emailService: AwsMailingService,
    private val runnable: RunnableEnvProperty,
) {
    @Async
    @TransactionalEventListener(SignupEmailAuthorizationEvent::class, phase = TransactionPhase.AFTER_COMPLETION)
    fun trigger(event: SignupEmailAuthorizationEvent) {
        if (runnable.signupAuthorizationEmail) {
            emailService.sendSignupAuthorizationEmail(event)
        }
    }
}