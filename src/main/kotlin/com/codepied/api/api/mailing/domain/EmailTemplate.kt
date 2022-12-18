package com.codepied.api.api.mailing.domain

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import javax.persistence.*

/**
 * Email Template Management Entity
 *
 * @author Aivyss
 * @since 2022/12/18
 */
@Entity
@Table(name = "EMAIL_TEMPLATE")
class EmailTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMPLATE_KEY")
    val id: Long,

    @Column(name = "TEMPLATE", nullable = false, updatable = false)
    val template: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "TEMPLATE_TYPE", nullable = false, updatable = false, unique = true)
    val type: EmailTemplateType,
)

interface EmailTemplateRepository : JpaRepository<EmailTemplate, Long> {
    fun findByType(type: EmailTemplateType): EmailTemplate?
}

fun EmailTemplateRepository.getByType(type: EmailTemplateType) : EmailTemplate {
    return this.findByType(type) ?: throwInvalidRequest(
        errorCode = ErrorCode.NO_RESOURCE_ERROR,
        debugMessage = "no email template error",
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    )
}
