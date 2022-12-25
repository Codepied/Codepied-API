package com.codepied.api.api.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EntityListeners

@Embeddable
@EntityListeners(AuditingEntityListener::class)
class Audit(
    @CreatedBy
    @Column(name = "CREATED_BY")
    var createdBy: Long? = null,

    @CreatedDate
    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null,

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    var lastModifiedBy: Long? = null,

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_AT")
    var lastModifiedAt: LocalDateTime? = null,

    @Column(name = "DELETED", nullable = false, updatable = true)
    var deleted: Boolean = false,
)