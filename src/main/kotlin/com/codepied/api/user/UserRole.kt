package com.codepied.api.domain

import com.codepied.api.api.role.Role
import javax.persistence.*

@Entity
@Table(name = "MST_USER_ROLE")
class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ROLE_KEY")
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    val user: User,

    @Enumerated(EnumType.STRING)
    val role: Role
)

object UserRoleFactory {
    fun create(role: Role, user: User): UserRole = UserRole(id = 0L, user = user, role = role)
}