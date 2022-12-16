package com.codepied.api.api.security

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.role.RoleType
import com.codepied.api.domain.UserFactory
import com.codepied.api.domain.UserRepository
import com.codepied.api.endpoint.dto.EmailUserCreate
import com.codepied.api.user.dto.EmailUserLogin
import com.codepied.api.user.domain.UserCredentialFactory
import com.codepied.api.user.domain.UserCredentialRepository
import com.codepied.api.user.domain.UserDetailsFactory
import com.codepied.api.user.domain.UserDetailsRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Email user login service
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Service
@Transactional
class EmailLoginServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val userCredentialRepository: UserCredentialRepository,
    private val userDetailsRepository: UserDetailsRepository,
    private val jwtService: JwtService,
): EmailLoginService {
    override fun login(request: EmailUserLogin): LoginInfo {
        val email = request.email

        val userInfo = userRepository.findEmailUser(email) ?: throwInvalidRequest(
            errorCode = ErrorCode.NO_SUCH_USER_LOGIN_ERROR,
            debugMessage = "not accessible user",
            httpStatus = HttpStatus.BAD_REQUEST,
        )

        if (!passwordEncoder.matches(request.password, userInfo.second.password)) {
            throwInvalidRequest(
                errorCode = ErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR,
                debugMessage = "not accessible user",
                httpStatus = HttpStatus.BAD_REQUEST,
            )
        }

        return LoginInfoImpl(
            user = userInfo.first.user,
            accessToken = jwtService.generateAccessToken(userInfo.first.user),
            refreshToken = jwtService.generateRefreshToken(userInfo.first.user),
            nickname = userInfo.first.nickname,
            userProfile = null,
            email = request.email,
        )
    }

    override fun signup(request: EmailUserCreate): LoginInfo {
        val encodedPw = passwordEncoder.encode(request.password)

        // * create user
        val user = UserFactory.createUser(request.email, listOf(RoleType.USER))
        userRepository.save(user)

        // * create social type
        user.addSocialIdentification(user.id.toString(), SocialType.EMAIL, request.email)

        // * create user credential
        val userCredential = UserCredentialFactory.create(encodedPw, user)
        userCredentialRepository.save(userCredential)

        // * create user details
        val userDetails = UserDetailsFactory.create(request.nickname, user)
        userDetailsRepository.save(userDetails)

        return LoginInfoImpl(
            user = user,
            accessToken = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user),
            nickname = userDetails.nickname,
            userProfile = null,
            email = request.email,
        )
    }
}