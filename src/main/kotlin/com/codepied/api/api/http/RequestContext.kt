package com.codepied.api.api.http

import com.codepied.api.api.role.RoleType
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

/**
 * request context (isolation level: thread => life cycle: request)
 *
 * @author Aivyss
 * @since 11/30/2022
 */
@Component
@Scope("request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestContext {
    var userKey: Long = -1
    var roleTypes: List<RoleType> = listOf()
    var supportLanguage: SupportLanguage = SupportLanguage.KO
    var validUser: Boolean = false
}