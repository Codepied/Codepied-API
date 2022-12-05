package com.codepied.api.api.security.dto

import com.codepied.api.api.role.Role
import org.springframework.security.core.GrantedAuthority

class CustomAuthority(val role: Role) : GrantedAuthority {
    override fun getAuthority(): String = role.name
}