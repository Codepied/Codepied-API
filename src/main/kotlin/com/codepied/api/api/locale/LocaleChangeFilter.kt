package com.codepied.api.api.locale

import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.LocaleResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * locale change interceptor
 *
 * @author Aivyss
 * @since 12/04/2022
 */
@Component
class LocaleChangeFilter(
    val localeResolver: LocaleResolver
): OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val locale = localeResolver.resolveLocale(request)
        localeResolver.setLocale(request, response, locale)

        return filterChain.doFilter(request, response)
    }
}