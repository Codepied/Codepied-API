package com.codepied.api.user.domain

import com.codepied.api.api.domain.Audit
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

/**
 * User Credential Management Entity for Email Type User
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "MST_USER_CREDENTIAL")
@EntityListeners(AuditingEntityListener::class)
class UserCredential(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CREDENTIAL_KEY")
    val id: Long,

    @Column(name = "PASSWORD", nullable = false, updatable = true)
    var password: String,

    @OneToOne
    @JoinColumn(name = "USER_KEY", nullable = false, updatable = false, unique = true)
    val user: User,

    @Embedded
    val audit: Audit = Audit(),
)

object UserCredentialFactory {
    fun create(password: String, user: User): UserCredential {
        return UserCredential(
            id = 0L,
            password = password,
            user = user,
        )
    }
}

interface UserCredentialRepository : JpaRepository<UserCredential, Long> {
    fun findByUserId(userId: Long): UserCredential?
}