package com.codepied.api.user.domain

import com.codepied.api.domain.User
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
class UserDetails(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_DETAILS_KEY")
    val id: Long,

    @Column(name = "NICKNAME", nullable = false, updatable = true, unique = true)
    var nickname: String,

    @Column(name = "STR_ID", nullable = false, updatable = false, unique = true)
    val uuid: UUID,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY", nullable = false, updatable = false, unique = true)
    val user: User,
)

object UserDetailsFactory {
    fun create(nickname: String, user: User): UserDetails {
        return UserDetails(
            id = 0L,
            nickname = nickname,
            uuid = UUID.randomUUID(),
            user = user,
        )
    }
}

interface UserDetailsRepository : JpaRepository<UserDetails, Long> {
    fun findByUser(user: User): UserDetails?
    fun findByUserId(userId: Long): UserDetails?
}