package com.codepied.api.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.role.RoleType
import com.codepied.api.user.domain.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * User Roles Management entity
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "MST_USER_ROLE")
@EntityListeners(AuditingEntityListener::class)
class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ROLE_KEY")
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_TYPE", nullable = false, updatable = false)
    val roleType: RoleType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    val user: User,

    @Embedded
    val audit: Audit = Audit(),
)

object UserRoleFactory {
    fun create(roleType: RoleType, user: User): UserRole = UserRole(id = 0L, user = user, roleType = roleType)
}