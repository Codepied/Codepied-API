package com.codepied.api.user.application

import com.codepied.api.api.TimeService
import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.api.mailing.event.SignupEmailAuthorizationEvent
import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.application.EmailLoginServiceImpl
import com.codepied.api.api.security.application.JwtService
import com.codepied.api.api.security.dto.EmailLoginInfoImpl
import com.codepied.api.api.security.event.LoginEvent
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.user.domain.*
import com.codepied.api.user.dto.EmailUserCreate
import com.codepied.api.user.dto.EmailUserLogin
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.ZonedDateTime

class EmailLoginServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var passwordEncoder: PasswordEncoder
    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var userCredentialRepository: UserCredentialRepository
    @Mock
    private lateinit var userDetailsRepository: UserDetailsRepository
    @Mock
    private lateinit var emailSignupAuthorizationRequestRepository: EmailSignupAuthorizationRequestRepository
    @Mock
    private lateinit var publishEventPublisher: ApplicationEventPublisher
    @Mock
    private lateinit var jwtService: JwtService
    @Mock
    private lateinit var eventPublisher: ApplicationEventPublisher
    @Mock
    private lateinit var timeService: TimeService

    @InjectMocks
    private lateinit var service: EmailLoginServiceImpl

    @Test
    fun `회원가입 서비스 성공`() {
        // * given
        val request = EmailUserCreate(
            email = "test@test.com",
            password = "password",
            nickname = "nickname",
        )
        val encodedPw = "encoded_pw"

        doReturn(false).`when`(userRepository).existsEmail(eq(request.email))
        doReturn(false).`when`(userRepository).existsNickname(eq(request.nickname))
        doReturn(encodedPw).`when`(passwordEncoder).encode(eq(request.password))
        val user = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.NOT_AUTHORIZED_BY_EMAIL)
        doReturn(user)
            .`when`(userRepository).save(any())
        val userDetails = UserDetailsFactory.create(request.nickname, user, null)
        doReturn(userDetails).`when`(userDetailsRepository).save(any())
        val auth = EmailSignupAuthorizationRequestFactory.create(user)
        doReturn(auth).`when`(emailSignupAuthorizationRequestRepository).save(any())
        lenient().doNothing().`when`(publishEventPublisher).publishEvent(any<SignupEmailAuthorizationEvent>())

        // * when
        val loginInfo = service.signup(request)

        // * then
        assertThat(loginInfo.getNickname()).isEqualTo(userDetails.nickname)
        assertThat(loginInfo.getEmail()).isEqualTo(request.email)
    }

    @Test
    fun `회원가입 실패 - 중복 이메일`() {
        // * given
        val request = EmailUserCreate(
            email = "test@test.com",
            password = "password",
            nickname = "nickname",
        )

        doReturn(true).`when`(userRepository).existsEmail(eq(request.email))

        // * when
        val throwable = catchThrowable { service.signup(request) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.DUPLICATED_EMAIL_SIGNUP)
    }

    @Test
    fun `회원가입 실패 - 닉네임 중복`() {
        // * given
        val request = EmailUserCreate(
            email = "test@test.com",
            password = "password",
            nickname = "nickname",
        )

        doReturn(false).`when`(userRepository).existsEmail(eq(request.email))
        doReturn(true).`when`(userRepository).existsNickname(eq(request.nickname))

        // * when
        val throwable = catchThrowable { service.signup(request) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.DUPLICATED_NICKNAME)
    }

    @Test
    fun `로그인 성공`() {
        // * given
        val request = EmailUserLogin(
            email = "test@test.com",
            password = "password"
        )

        val userDetails = Mockito.mock(UserDetails::class.java)
        val userCredential = Mockito.mock(UserCredential::class.java)
        val user = Mockito.mock(User::class.java)
        val credentialAudit = Mockito.mock(Audit::class.java)
        val now = ZonedDateTime.now()

        doReturn(user).`when`(userDetails).user
        doReturn("nickname").`when`(userDetails).nickname
        doReturn("encodedPw").`when`(userCredential).password
        doReturn(credentialAudit).`when`(userCredential).audit
        doReturn(now.minusDays(100).toLocalDateTime()).`when`(credentialAudit).lastModifiedAt
        doReturn(Pair(userDetails, userCredential)).`when`(userRepository).findEmailUser(anyString())
        doReturn(ActivateStatus.ACTIVATED).`when`(user).activateStatus
        doReturn(1L).`when`(user).id
        doReturn(true).`when`(passwordEncoder).matches(anyString(), anyString())
        doReturn("access_token").`when`(jwtService).generateAccessToken(any(), any())
        doReturn("refresh_token").`when`(jwtService).generateRefreshToken(any())
        lenient().doNothing().`when`(eventPublisher).publishEvent(any<LoginEvent>())

        doReturn(now).`when`(timeService).now()

        // * when
        val loginInfo = service.login(request)

        // * then
        assertThat(loginInfo).isNotNull
        assertThat(loginInfo is EmailLoginInfoImpl).isTrue
        val info = loginInfo as EmailLoginInfoImpl
        assertThat(info.passwordChangeRecommended).isTrue
    }

    @Test
    fun `로그인 실패 - 유저 없음`() {
        // * given
        val request = EmailUserLogin(
            email = "test@test.com",
            password = "password"
        )

        doReturn(null).`when`(userRepository).findEmailUser(anyString())

        // * when
        val throwable = catchThrowable { service.login(request) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.NO_SUCH_USER_LOGIN_ERROR)
    }

    @Test
    fun `로그인 실패 - 이메일 인증을 수행하지 않음`() {
        // * given
        val request = EmailUserLogin(
            email = "test@test.com",
            password = "password"
        )

        val userDetails = Mockito.mock(UserDetails::class.java)
        val userCredential = Mockito.mock(UserCredential::class.java)
        val user = Mockito.mock(User::class.java)
        doReturn(user).`when`(userDetails).user
        doReturn(Pair(userDetails, userCredential)).`when`(userRepository).findEmailUser(anyString())
        lenient().doNothing().`when`(eventPublisher).publishEvent(any<LoginEvent>())
        doReturn(ActivateStatus.NOT_AUTHORIZED_BY_EMAIL).`when`(user).activateStatus

        // * when
        val throwable = catchThrowable { service.login(request) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.NOT_AUTHORIZED_EMAIL_USER)
    }

    @Test
    fun `로그인 실패 - 패스워스 불일치`() {
        // * given
        val request = EmailUserLogin(
            email = "test@test.com",
            password = "password"
        )

        val userDetails = Mockito.mock(UserDetails::class.java)
        val userCredential = Mockito.mock(UserCredential::class.java)
        val user = Mockito.mock(User::class.java)
        doReturn(user).`when`(userDetails).user
        doReturn("encodedPw").`when`(userCredential).password
        doReturn(Pair(userDetails, userCredential)).`when`(userRepository).findEmailUser(anyString())
        doReturn(ActivateStatus.ACTIVATED).`when`(user).activateStatus
        doReturn(false).`when`(passwordEncoder).matches(anyString(), anyString())
        lenient().doNothing().`when`(eventPublisher).publishEvent(any<LoginEvent>())

        // * when
        val throwable = catchThrowable { service.login(request) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR)
    }
}