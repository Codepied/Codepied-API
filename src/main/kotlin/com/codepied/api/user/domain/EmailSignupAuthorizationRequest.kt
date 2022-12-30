package com.codepied.api.user.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.*

/**
 * Email Authorization Managment entity
 *
 * @author Aivyss
 * @since 2022/12/18
 */
@Entity
@Table(name = "EMAIL_SIGNUP_AUTH")
@EntityListeners(AuditingEntityListener::class)
class EmailSignupAuthorizationRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_KEY")
    val id: Long,

    @OneToOne
    @JoinColumn(name = "USER_KEY")
    val user: User,
) {
    @Column(name = "UUID", updatable = false, nullable = false, unique = true)
    val uuid: UUID = UUID.randomUUID()

    @Embedded
    val audit: Audit = Audit()
}

object EmailSignupAuthorizationRequestFactory {
    fun create(user: User): EmailSignupAuthorizationRequest {
        return EmailSignupAuthorizationRequest(
            id = 0L,
            user = user
        )
    }

}

interface EmailSignupAuthorizationRequestRepository : JpaRepository<EmailSignupAuthorizationRequest, Long> {
    fun findByUuid(uuid: UUID): EmailSignupAuthorizationRequest?
}

fun EmailSignupAuthorizationRequestRepository.getByUuid(uuid: UUID): EmailSignupAuthorizationRequest {
    return this.findByUuid(uuid) ?: throwInvalidRequest(
        errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
        debugMessage = "invalid email auth uuid"
    )
}