package com.codepied.api.api.security

import com.codepied.api.api.ObjectMapperHolder
import com.codepied.api.api.TimeService
import com.codepied.api.api.config.JwtProperty
import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.security.dto.PrincipalDetails
import com.codepied.api.domain.User
import com.fasterxml.jackson.core.type.TypeReference
import io.jsonwebtoken.*
import org.springframework.stereotype.Service
import java.util.*

/**
 * Jwt Service
 *
 * @author Aivyss
 * @since 12/02/2022
 */
@Service
class JwtService(
    private val jwtProperty: JwtProperty,
    private val timeService: TimeService,
) {
    fun generateAccessToken(user: User): String {
        val principalDetails = PrincipalDetails.from(user)
        val now = Date.from(timeService.now().toInstant())
        val expireTime = Date(now.time + jwtProperty.accessTokenLifetime)

        return Jwts.builder()
            .setSubject(ObjectMapperHolder.writeValueAsString(principalDetails))
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS512, jwtProperty.accessTokenSecret.toByteArray(Charsets.UTF_8))
            .compact()
    }

    fun generateRefreshToken(user: User): String {
        val now = Date.from(timeService.now().toInstant())
        val expireTime = Date(now.time + jwtProperty.refreshTokenLifetime)

        return Jwts.builder()
            .setSubject(user.id.toString())
            .setIssuedAt(Date.from(timeService.now().toInstant()))
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS512, jwtProperty.refreshTokenSecret.toByteArray(Charsets.UTF_8))
            .compact()
    }

    fun parseAccessToken(jwt: String) = ObjectMapperHolder.readValue(claimsOfAccessToken(jwt).subject, object : TypeReference<PrincipalDetails>() {})

    private fun claimsOfAccessToken(jwt: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(jwtProperty.accessTokenSecret.toByteArray(Charsets.UTF_8))
                .parseClaimsJws(jwt)
                .body
        } catch (e: ExpiredJwtException) {
            throwInvalidRequest(
                errorCode = ErrorCode.ACCESS_TOKEN_EXPIRED,
                debugMessage = "access token expired",
            )
        } catch (e: Exception) {
            when(e) {
                is UnsupportedJwtException, is MalformedJwtException -> {
                    throwInvalidRequest(
                        errorCode = ErrorCode.INVALID_ACCESS_TOKEN,
                        debugMessage = "Invalid access token was used ",
                    )
                }
                else -> {
                    throwInvalidRequest(
                        errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
                        debugMessage = "unknown error during jwt parsing",
                    )
                }
            }
        }
    }
}