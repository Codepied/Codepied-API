package com.codepied.api.user.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.domain.QSocialUserIdentification.socialUserIdentification
import com.codepied.api.user.domain.QUser.user as  userAlias
import com.codepied.api.user.domain.QUserCredential.userCredential
import com.codepied.api.user.domain.QUserDetails.userDetails
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import javax.persistence.*

/**
 * User Entity
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "MST_USER")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_KEY")
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVATE_STATUS", nullable = false, updatable = true)
    var activateStatus: ActivateStatus
) {
    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val roles: MutableList<UserRole> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val socialIdentifications: MutableList<SocialUserIdentification> = mutableListOf()

    @Embedded
    var audit: Audit = Audit()

    fun addRole(roleType: RoleType): User {
        val userRole = UserRoleFactory.create(roleType = roleType, user = this)
        roles += userRole

        return this
    }

    fun addSocialIdentification(identification: String, socialType: SocialType, email: String? = null): User {
        this.socialIdentifications += SocialUserIdentificationFactory.create(
            socialIdentification = identification,
            socialType = socialType,
            user = this,
            email = email,
        )

        return this
    }
}

object UserFactory {
    fun createUser(roleTypes: List<RoleType>, activateStatus: ActivateStatus): User {
        val user = User(
            id = 0L,
            activateStatus = activateStatus
        )

        return roleTypes.forEach { user.addRole(it) }.let { user }
    }
}

interface UserRepository : JpaRepository<User, Long>, UserQueryRepository {
}

fun UserRepository.getUserById(id: Long): User {
    return this.findById(id).orElseThrow {
        invalidRequest(
            errorCode = BusinessErrorCode.INVALID_ACCESS_USER,
            debugMessage = "no found user")
    }
}

interface UserQueryRepository {
    fun findEmailUser(email: String): Pair<UserDetails, UserCredential>?
    fun existsEmail(email: String): Boolean
    fun existsNickname(nickname: String): Boolean
}

@Component
class UserQueryRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : UserQueryRepository {
    override fun findEmailUser(email: String): Pair<UserDetails, UserCredential>? {
        val socialUser = jpaQueryFactory.select(socialUserIdentification)
            .from(socialUserIdentification)
            .innerJoin(socialUserIdentification.user, userAlias).fetchJoin()
            .where(
                socialUserIdentification.email.isNotNull,
                socialUserIdentification.socialType.eq(SocialType.EMAIL),
                socialUserIdentification.email.eq(email),
            ).fetchOne() ?: return null
        val userDetailsEntity = jpaQueryFactory.select(userDetails)
            .from(userDetails)
            .innerJoin(userDetails.user, userAlias)
            .where(userDetails.user.eq(socialUser.user))
            .fetchOne() ?: return null

        val userCredential = jpaQueryFactory.select(userCredential)
            .from(userCredential)
            .where(userCredential.user.eq(socialUser.user)).fetchOne() ?: return null

        return userDetailsEntity to userCredential
    }

    override fun existsEmail(email: String): Boolean {
        return jpaQueryFactory
            .select(socialUserIdentification)
            .from(socialUserIdentification)
            .where(
                socialUserIdentification.email.eq(email)
            )
            .fetch().size > 0
    }

    override fun existsNickname(nickname: String): Boolean {
        return jpaQueryFactory.select(userDetails.count())
            .from(userDetails)
            .where(userDetails.nickname.eq(nickname))
            .fetchOne() ?: 1L == 1L
    }
}