package com.codepied.api.api.mailing.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.ServerExceptionBuilder.throwInternalServerError
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

/**
 * Email Template Management Entity
 *
 * @author Aivyss
 * @since 2022/12/18
 */
@Entity
@Table(name = "EMAIL_TEMPLATE")
@EntityListeners(AuditingEntityListener::class)
class EmailTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMPLATE_KEY")
    val id: Long,

    @Column(name = "TITLE", nullable = false, updatable = false)
    val title: String,

    @Column(name = "TEMPLATE", nullable = false, updatable = false)
    val template: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "TEMPLATE_TYPE", nullable = false, updatable = false, unique = true)
    val type: EmailTemplateType,

    @Embedded
    val audit: Audit = Audit(),
)

interface EmailTemplateRepository : JpaRepository<EmailTemplate, Long> {
    fun findByType(type: EmailTemplateType): EmailTemplate?
    fun getByType(type: EmailTemplateType) : EmailTemplate {
        return this.findByType(type) ?: throwInternalServerError("no email template")
    }
}