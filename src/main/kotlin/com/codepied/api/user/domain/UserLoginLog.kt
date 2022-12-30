package com.codepied.api.user.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.security.event.LoginStatus
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(name = "MST_USER_LOGIN_LOG")
@EntityListeners(AuditingEntityListener::class)
class UserLoginLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_KEY")
    val id: Long,

    @Column(name = "USER_KEY", nullable = false, updatable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, updatable = false)
    val status: LoginStatus,
) {
    @Embedded
    val audit: Audit = Audit()
}


object UserLoginLogFactory {
    fun create(userId: Long, status: LoginStatus, id: Long = 0L): UserLoginLog = UserLoginLog(id, userId, status)
}

interface UserLoginLogRepository : JpaRepository<UserLoginLog, Long>