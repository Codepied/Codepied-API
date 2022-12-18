package com.codepied.api.api.security.dto

import com.codepied.api.api.role.RoleType
import org.springframework.security.core.GrantedAuthority

/**
 * Authentication
 *
 * @author Aivyss
 * @since 2022/12/17
 */
class CustomAuthority(val roleType: RoleType) : GrantedAuthority {
    override fun getAuthority(): String = roleType.name
}