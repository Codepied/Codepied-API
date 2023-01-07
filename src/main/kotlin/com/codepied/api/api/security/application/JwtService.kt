package com.codepied.api.api.security.application

import com.codepied.api.api.TimeService
import com.codepied.api.api.config.JwtProperty
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.exception.ServerExceptionBuilder.throwInternalServerError
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.*
import com.codepied.api.user.domain.User
import com.codepied.api.user.domain.UserDetailsRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
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
    private val objectMapper: ObjectMapper,
    private val userDetailsRepository: UserDetailsRepository,
) {
    fun generateAccessToken(user: User, socialType: SocialType): String {
        val principalDetails = PrincipalDetails.from(user, socialType)
        val now = Date.from(timeService.now().toInstant())
        val expireTime = Date(now.time + jwtProperty.accessTokenLifetime)

        return Jwts.builder()
            .setSubject(objectMapper.writeValueAsString(principalDetails))
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS512, jwtProperty.accessTokenSecret.toByteArray(Charsets.UTF_8))
            .compact()
    }

    fun generateRefreshToken(user: User, socialType: SocialType): String {
        val now = Date.from(timeService.now().toInstant())
        val expireTime = Date(now.time + jwtProperty.refreshTokenLifetime)

        return Jwts.builder()
            .setSubject(objectMapper.writeValueAsString(RefreshTokenBody(
                userKey = user.id,
                socialType = socialType,
            )))
            .setIssuedAt(Date.from(timeService.now().toInstant()))
            .setIssuedAt(now)
            .setExpiration(expireTime)
            .signWith(SignatureAlgorithm.HS512, jwtProperty.refreshTokenSecret.toByteArray(Charsets.UTF_8))
            .compact()
    }

    fun refreshTokens(refreshToken: String): LoginInfo {
        val tokenBody = parseRefreshToken(refreshToken)
        val userDetails = userDetailsRepository.getByUserId(tokenBody.userKey)
        val user = userDetails.user

        return LoginInfoImpl(
            userKey = user.id,
            accessToken = this.generateAccessToken(user, tokenBody.socialType),
            refreshToken = this.generateRefreshToken(user, tokenBody.socialType),
            nickname = userDetails.nickname,
            userProfile = userDetails.profileFileId,
            email = user.socialIdentifications[0].email,
        )
    }

    fun parseAccessToken(jwt: String): PrincipalDetails = objectMapper.readValue(
        claimsOfAccessToken(jwt).subject,
        object : TypeReference<PrincipalDetails>() {}
    )

    fun parseRefreshToken(jwt: String): RefreshTokenBody = objectMapper.readValue(
        claimsOfRefreshToken(jwt).subject,
        object: TypeReference<RefreshTokenBody>() {},
    )

    private fun claimsOfRefreshToken(jwt: String): Claims {
        return try {
            Jwts.parser()
                .setSigningKey(jwtProperty.refreshTokenSecret.toByteArray(Charsets.UTF_8))
                .parseClaimsJws(jwt)
                .body
        } catch (e: ExpiredJwtException) {
            throwInvalidRequest(
                errorCode = BusinessErrorCode.ACCESS_TOKEN_EXPIRED,
                debugMessage = "refresh token expired",
            )
        } catch (e: Exception) {
            when(e) {
                is UnsupportedJwtException, is MalformedJwtException -> {
                    throwInvalidRequest(
                        errorCode = BusinessErrorCode.INVALID_ACCESS_TOKEN,
                        debugMessage = "Invalid refresh token was used ",
                    )
                }

                else -> throwInternalServerError(debugMessage = "unknown error then parsing access token")
            }
        }
    }

    private fun claimsOfAccessToken(jwt: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(jwtProperty.accessTokenSecret.toByteArray(Charsets.UTF_8))
                .parseClaimsJws(jwt)
                .body
        } catch (e: ExpiredJwtException) {
            throwInvalidRequest(
                errorCode = BusinessErrorCode.ACCESS_TOKEN_EXPIRED,
                debugMessage = "access token expired",
            )
        } catch (e: Exception) {
            when(e) {
                is UnsupportedJwtException, is MalformedJwtException -> {
                    throwInvalidRequest(
                        errorCode = BusinessErrorCode.INVALID_ACCESS_TOKEN,
                        debugMessage = "Invalid access token was used ",
                    )
                }

                else -> throwInternalServerError(debugMessage = "unknown error then parsing access token")
            }
        }
    }
}