package com.codepied.api.api.security.dto

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

/**
 * Authentication
 *
 * @author Aivyss
 * @since 2022/12/17
 */
class CustomAuthentication(
    private val principal: PrincipalDetails,
    private var authenticated: Boolean,
) : Authentication {
    override fun getName(): String = ""

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()

        return principal.roleTypes.forEach { authorities += CustomAuthority(it) }.let { authorities }
    }

    override fun getCredentials() = ""

    override fun getDetails(): PrincipalDetails = principal

    override fun getPrincipal(): PrincipalDetails = principal

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}