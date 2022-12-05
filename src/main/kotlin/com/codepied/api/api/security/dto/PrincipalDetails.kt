package com.codepied.api.api.security.dto

import com.codepied.api.api.role.Role
import com.codepied.api.domain.User
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class PrincipalDetails(
    val userKey: Long,
    val roles: List<Role>,
) : UserDetails {
    companion object {
        fun from(user: User): PrincipalDetails = user.let {
            PrincipalDetails(
                userKey = it.id,
                roles = it.roles.map { entity -> entity.role },
            )
        }
    }

    @JsonIgnore
    fun createAuthentication(): Authentication = CustomAuthentication(this, true)

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        roles.forEach {  authorities += CustomAuthority(it) }

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