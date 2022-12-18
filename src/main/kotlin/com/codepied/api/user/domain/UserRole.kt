package com.codepied.api.domain

import com.codepied.api.api.role.RoleType
import javax.persistence.*

/**
 * User Roles Management entity
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Entity
@Table(name = "MST_USER_ROLE")
class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ROLE_KEY")
    val id: Long,

    @Enumerated(EnumType.STRING)
    val roleType: RoleType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    val user: User,

)

object UserRoleFactory {
    fun create(roleType: RoleType, user: User): UserRole = UserRole(id = 0L, user = user, roleType = roleType)
}