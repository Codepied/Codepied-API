package com.codepied.api.api.security.application

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.externalApi.SocialLoginApiService
import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.dto.SocialAccount
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.domain.User
import com.codepied.api.user.domain.UserFactory
import com.codepied.api.user.domain.UserRepository
import com.codepied.api.user.domain.ActivateStatus
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.domain.UserDetailsFactory
import com.codepied.api.user.domain.UserDetailsRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SocialLoginServiceImpl(
    private val externalApiService: SocialLoginApiService,
    private val socialUserIdentificationRepository: SocialUserIdentificationRepository,
    private val userDetailsRepository: UserDetailsRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
): SocialLoginService {
    override fun signup(socialType: SocialType, socialAccount: SocialAccount): LoginInfo {
        val user = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.ACTIVATED)
        user.addSocialIdentification(socialAccount.socialIdentification(), socialType, socialAccount.email())
        userRepository.save(user)

        val userDetails = UserDetailsFactory.create("유동-${UUID.randomUUID()}", user)
        userDetailsRepository.save(userDetails)

        return LoginInfoImpl(
            userKey = user.id,
            accessToken = jwtService.generateAccessToken(user),
            refreshToken = jwtService.generateRefreshToken(user),
            nickname = userDetails.nickname,
            userProfile = null,
            email = socialAccount.email(),
        )
    }

    override fun login(socialType: SocialType, authorizationCode: String): LoginInfo {
        val socialAccount = externalApiService.loginAuthorization(socialType, authorizationCode)
        val socialIdentification =
            socialUserIdentificationRepository.findBySocialTypeAndSocialIdentification(
                socialType,
                socialAccount.socialIdentification()
            )

        // * signup
        return if (socialIdentification == null) {
            this.signup(socialType, socialAccount)
        } else {
            val user = socialIdentification.user
            val userDetails = userDetailsRepository.findByUser(user) ?: throwInvalidRequest(
                errorCode = BusinessErrorCode.NO_SUCH_USER_LOGIN_ERROR,
                debugMessage = "not accessible user",
                httpStatus = HttpStatus.BAD_REQUEST,
            )

            LoginInfoImpl(
                userKey = user.id,
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = jwtService.generateRefreshToken(user),
                nickname = userDetails.nickname,
                userProfile = null,
                email = socialIdentification.email,
            )
        }
    }

    override fun logout(socialType: SocialType, authorizationCode: String, user: User) {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }
}