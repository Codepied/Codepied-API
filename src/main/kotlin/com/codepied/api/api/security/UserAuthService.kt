package com.codepied.api.api.security

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.role.Role
import com.codepied.api.api.security.dto.CustomAuthentication
import com.codepied.api.api.security.dto.PrincipalDetails
import com.codepied.api.domain.UserFactory
import com.codepied.api.domain.UserLanguageFactory
import com.codepied.api.domain.UserLanguageRepository
import com.codepied.api.domain.UserRepository
import com.codepied.api.endpoint.dto.EmailUserCreate
import com.codepied.api.endpoint.dto.EmailUserLogin
import com.codepied.api.endpoint.dto.LoginSuccessUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletResponse

/**
 * User Auth Test Service Bean
 *
 * @author Aivyss
 * @since 12/01/2022
 */
@Service
class UserAuthService(
    private val userRepository: UserRepository,
    private val userLanguageRepository: UserLanguageRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val requestContext: RequestContext,
) {
    fun signup(request: EmailUserCreate): Long {
        if (userRepository.existsByEmailAndDeletedFalse(request.email)) {
            throwInvalidRequest(
                errorCode = ErrorCode.SIGN_UP_DUPLICATE_EMAIL_ERROR,
                debugMessage = "duplicated email",
            )
        }

        val userEntity = request.let {
            UserFactory.create(
                email = it.email,
                password = passwordEncoder.encode(request.password),
                username = it.username,
                roles = mutableListOf(Role.USER)
            )
        }
        userRepository.save(userEntity)

        val userLanguage = UserLanguageFactory.create(user = userEntity, requestContext.supportLanguage)

        return userLanguageRepository.save(userLanguage).user.id
    }

    fun emailUserLogin(request: EmailUserLogin, response: HttpServletResponse): LoginSuccessUser {
        val user = userRepository.findByEmailAndDeletedFalse(request.email)
            ?: throw invalidRequest(
                errorCode = ErrorCode.NO_SUCH_USER_LOGIN_ERROR,
                debugMessage = "no such user by email login",
            )

        val dbPassword = user.password

        if (!passwordEncoder.matches(request.password, dbPassword)) {
            throw invalidRequest(
                errorCode = ErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR,
                debugMessage = "no same password",
            )
        }

        val authentication = CustomAuthentication(PrincipalDetails(
            userKey = user.id,
            roles = user.roles.map { it.role }
        ), true)
        SecurityContextHolder.getContext().authentication = authentication
        val responseDto = LoginSuccessUser(
            userKey = user.id,
            accessToken = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user),
            roles = user.roles.map { it.role }
        )
        response.setHeader("Authorization", "Bearer ${responseDto.accessToken}")
        response.setHeader("refresh", "Bearer ${responseDto.refreshToken}")

        return responseDto
    }
}