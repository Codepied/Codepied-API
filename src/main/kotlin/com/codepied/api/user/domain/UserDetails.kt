package com.codepied.api.user.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwNoSuchUser
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import javax.persistence.*

/**
 * User info entity
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "MST_USER_DETAILS")
@EntityListeners(AuditingEntityListener::class)
class UserDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_DETAILS_KEY")
    val id: Long,

    @Column(name = "NICKNAME", nullable = false, updatable = true, unique = true)
    var nickname: String,

    @Column(name = "STR_ID", nullable = false, updatable = false, unique = true)
    val uuid: UUID,

    @Column(name = "PROFILE", nullable = true, updatable = true, unique = true)
    var profileFileId: String?,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY", nullable = false, updatable = false, unique = true)
    val user: User,


    @Embedded
    val audit: Audit = Audit(),
)

object UserDetailsFactory {
    fun create(nickname: String, user: User, profileFileId: String?): UserDetails {
        return UserDetails(
            id = 0L,
            nickname = nickname,
            uuid = UUID.randomUUID(),
            user = user,
            profileFileId = profileFileId,
        )
    }
}

interface UserDetailsRepository : JpaRepository<UserDetails, Long> {
    fun findByUser(user: User): UserDetails?
    fun findByUserId(userId: Long): UserDetails?
    fun getByUserId(userId: Long): UserDetails = this.findByUserId(userId) ?: throwNoSuchUser()
    fun existsByNickname(nickname: String): Boolean
}