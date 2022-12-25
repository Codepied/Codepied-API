package com.codepied.api.api.domain

import com.codepied.api.api.security.dto.CustomAuthentication
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomAuditAware : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication: Authentication? = SecurityContextHolder.getContext()?.authentication

        val userKey = if (authentication is CustomAuthentication) {
            authentication.details.userKey
        } else {
            null
        }

        return Optional.ofNullable(userKey)
    }
}