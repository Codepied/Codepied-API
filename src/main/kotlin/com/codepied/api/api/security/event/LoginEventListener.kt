package com.codepied.api.api.security.event

import com.codepied.api.user.domain.UserLoginLogFactory
import com.codepied.api.user.domain.UserLoginLogRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

/**
 * login log event listener
 *
 * @author Aivyss
 * @since 2022/12/31
 */
@Component
class LoginEventListener(
    private val loginLogRepository: UserLoginLogRepository,
) {
    @Async
    @TransactionalEventListener(LoginEvent::class, phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun loginEvent(event: LoginEvent) {
        loginLogRepository.save(UserLoginLogFactory.create(event.userId, event.status))
    }
}