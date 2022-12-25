package com.codepied.api.user.domain

import com.codepied.api.api.security.SocialType
import com.codepied.api.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

/**
 * Social Type Management entity
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "SOCIAL_USER_IDENTI")
class SocialUserIdentification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "IDENTIFICATION", updatable = false, nullable = false, unique = true)
    val socialIdentification: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "SOCIAL_TYPE", updatable = false, nullable = false)
    val socialType: SocialType,

    @ManyToOne
    @JoinColumn(name = "USER_KEY", updatable = false, nullable = false)
    val user: User,

    var email: String?
)

object SocialUserIdentificationFactory {
    fun create(socialIdentification: String, socialType: SocialType, user: User, email: String? = null): SocialUserIdentification {
        return SocialUserIdentification(
            id = 0L,
            socialIdentification = socialIdentification,
            socialType = socialType,
            user = user,
            email = email,
        )
    }
}

interface SocialUserIdentificationRepository : JpaRepository<SocialUserIdentification, Long> {
    fun findBySocialTypeAndSocialIdentification(socialType: SocialType, socialIdentification: String): SocialUserIdentification?
    fun existsByEmail(email: String): Boolean
}