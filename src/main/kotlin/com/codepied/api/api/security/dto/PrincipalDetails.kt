package com.codepied.api.api.security.dto

import com.codepied.api.api.role.RoleType
import com.codepied.api.domain.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Principal for Spring Security
 *
 * @author Aivyss
 * @since 2022/12/17
 */
data class PrincipalDetails(
    val userKey: Long,
    val roleTypes: List<RoleType>,
) : UserDetails {
    companion object {
        fun from(user: User): PrincipalDetails = user.let {
            PrincipalDetails(
                userKey = it.id,
                roleTypes = it.roles.map { entity -> entity.roleType },
            )
        }
    }

    @JsonIgnore
    fun createAuthentication(): Authentication = CustomAuthentication(this, true)

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        roleTypes.forEach {  authorities += CustomAuthority(it) }

        return authorities
    }
    @JsonIgnore
    override fun getPassword(): String = ""
    @JsonIgnore
    override fun getUsername(): String = userKey.toString()
    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = true
    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = true
    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = true
    @JsonIgnore
    override fun isEnabled(): Boolean = true
}