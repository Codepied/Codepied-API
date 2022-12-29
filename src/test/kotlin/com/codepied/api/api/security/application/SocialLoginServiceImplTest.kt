package com.codepied.api.api.security.application

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.api.externalApi.SocialLoginApiService
import com.codepied.api.api.externalApi.dto.SocialAccountImpl
import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.user.domain.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import java.util.*

class SocialLoginServiceImplTest : AbstractServiceTest() {
    @Mock
    private lateinit var externalApiService: SocialLoginApiService
    @Mock
    private lateinit var socialUserIdentificationRepository: SocialUserIdentificationRepository
    @Mock
    private lateinit var userDetailsRepository: UserDetailsRepository
    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var jwtService: JwtService

    @InjectMocks
    private lateinit var service: SocialLoginServiceImpl

    @Test
    fun `소셜 회원가입 - 성공`() {
        // * given
        val socialType = SocialType.GOOGLE
        val socialAccount = SocialAccountImpl(
            id = "102948912839",
            email = "test@gmail.com"
        )

        val user = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.ACTIVATED)
        doReturn(user).`when`(userRepository).save(any())

        val userDetails = UserDetailsFactory.create("유동-${UUID.randomUUID()}", user)
        doReturn(userDetails).`when`(userDetailsRepository).save(any())

        val accessToken = "access token"
        val refreshToken = "refresh token"
        doReturn(accessToken).`when`(jwtService).generateAccessToken(any())
        doReturn(refreshToken).`when`(jwtService).generateRefreshToken(any())

        // * when
        val loginInfo = service.signup(socialType, socialAccount)

        // * then
        assertThat(loginInfo.getAccessToken()).isEqualTo(accessToken)
        assertThat(loginInfo.getRefreshToken()).isEqualTo(refreshToken)
        assertThat(loginInfo.getEmail()).isEqualTo(socialAccount.email())
    }

    @Test
    fun `로그인 - 회원가입이 되어 있지 않은 경우`() {
        // * given
        val socialType = SocialType.GOOGLE
        val socialAccount = SocialAccountImpl(
            id = "102948912839",
            email = "test@gmail.com"
        )
        val authorizationCode = "authorization code"
        doReturn(socialAccount).`when`(externalApiService).loginAuthorization(eq(socialType), eq(authorizationCode))
        doReturn(null).`when`(socialUserIdentificationRepository).findBySocialTypeAndSocialIdentification(
            eq(socialType),
            eq(socialAccount.socialIdentification())
        )

        this.`소셜 회원가입 - 성공`()

        // * when
        val loginInfo = service.login(socialType, authorizationCode)

        // * then
        assertThat(loginInfo.getEmail()).isEqualTo(socialAccount.email())
    }

    @Test
    fun `로그인 성공 - 가입된 유저`() {
        // * given
        val socialType = SocialType.GOOGLE
        val socialAccount = SocialAccountImpl(
            id = "102948912839",
            email = "test@gmail.com"
        )
        val authorizationCode = "authorization code"
        doReturn(socialAccount).`when`(externalApiService).loginAuthorization(eq(socialType), eq(authorizationCode))

        val user = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.ACTIVATED)
        user.addSocialIdentification(socialAccount.socialIdentification(), socialType, socialAccount.email())
        doReturn(user.socialIdentifications[0]).`when`(socialUserIdentificationRepository).findBySocialTypeAndSocialIdentification(
            eq(socialType),
            eq(socialAccount.socialIdentification())
        )

        val userDetails = UserDetailsFactory.create("nickname", user)
        doReturn(userDetails).`when`(userDetailsRepository).findByUser(eq(user))

        val accessToken = "access token"
        val refreshToken = "refresh token"
        doReturn(accessToken).`when`(jwtService).generateAccessToken(any())
        doReturn(refreshToken).`when`(jwtService).generateRefreshToken(any())

        // * when
        val loginInfo = service.login(socialType, authorizationCode)

        assertThat(loginInfo.getEmail()).isEqualTo(socialAccount.email())
        assertThat(loginInfo.getAccessToken()).isEqualTo(accessToken)
        assertThat(loginInfo.getRefreshToken()).isEqualTo(refreshToken)
        assertThat(loginInfo.getNickname()).isEqualTo(userDetails.nickname)
        assertThat(loginInfo.getEmail()).isEqualTo(socialAccount.email())
    }

    @Test
    fun `로그인 실패 - userDetails 존재하지 않음 (사실 정상 작동시 없는 케이스)`() {
        // * given
        val socialType = SocialType.GOOGLE
        val socialAccount = SocialAccountImpl(
            id = "102948912839",
            email = "test@gmail.com"
        )
        val authorizationCode = "authorization code"
        doReturn(socialAccount).`when`(externalApiService).loginAuthorization(eq(socialType), eq(authorizationCode))

        val user = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.ACTIVATED)
        user.addSocialIdentification(socialAccount.socialIdentification(), socialType, socialAccount.email())
        doReturn(user.socialIdentifications[0]).`when`(socialUserIdentificationRepository).findBySocialTypeAndSocialIdentification(
            eq(socialType),
            eq(socialAccount.socialIdentification())
        )

        doReturn(null).`when`(userDetailsRepository).findByUser(eq(user))

        // * when
        val throwable = catchThrowable { service.login(socialType, authorizationCode) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.NO_SUCH_USER_LOGIN_ERROR)
    }
}