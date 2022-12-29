package com.codepied.api.api.security

import com.codepied.api.api.exception.CodepiedBaseException
import com.codepied.api.api.exception.ExceptionController
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.security.application.JwtService
import com.codepied.api.api.security.dto.JwtAuthExcludeUrlPattern
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Jwt Authorization Filter
 *
 * @author Aivyss
 * @since 12/03/2022
 */
@Component
class JwtAuthFilter(
    val jwtService: JwtService,
    val exceptionController: ExceptionController,
    val requestContext: RequestContext,
    val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {
    private val excludeUrlPatterns: List<String> = JwtAuthExcludeUrlPattern.values().map { it.url }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val accessToken = request.getHeader("Authorization").replace("Bearer ", "")
            val principalDetails = jwtService.parseAccessToken(accessToken).also {
                requestContext.roleTypes = it.roleTypes
                requestContext.userKey = it.userKey
                requestContext.validUser = true
            }
            val authentication = principalDetails.createAuthentication()
            SecurityContextHolder.getContext().authentication = authentication

            filterChain.doFilter(request, response)
        } catch (e: CodepiedBaseException.InvalidRequestException) {
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.writer.write(objectMapper.writeValueAsString(exceptionController.invalidRequestException(e)))
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI in excludeUrlPatterns
    }
}