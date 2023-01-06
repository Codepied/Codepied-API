package com.codepied.api.user.application

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.api.externalApi.SocialLoginApiService
import com.codepied.api.api.externalApi.dto.SocialAccountImpl
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.security.SocialType
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.user.domain.SocialUserIdentificationFactory
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.domain.User
import com.codepied.api.user.persist.UserIntegrationNativeQueryRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.*

internal class UserIntegrationServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var requestContext: RequestContext
    @Mock
    private lateinit var socialLoginApiService: SocialLoginApiService
    @Mock
    private lateinit var socialUserIdentificationRepository: SocialUserIdentificationRepository
    @Mock
    private lateinit var nativeQueryRepository: UserIntegrationNativeQueryRepository

    @InjectMocks
    private lateinit var service: UserIntegrationService

    @BeforeEach
    fun init() {
        doReturn(1L).`when`(requestContext).userKey
    }

    @Test
    fun `유저 통합 성공 (이미 통합된 케이스)`() {
        // * given
        val socialType = SocialType.KAKAO
        val authorizationCode = "ofijaiowef.foaiwsejfoa.asodfijaweo"
        val socialAccount = SocialAccountImpl(
            id = "oklfjawo;eifj",
            email = "test@kakao.com",
        )
        doReturn(socialAccount).`when`(socialLoginApiService).loginAuthorization(eq(socialType), eq(authorizationCode))
        val user = Mockito.mock(User::class.java)
        val socialUser = SocialUserIdentificationFactory.create(
            socialIdentification = socialAccount.socialIdentification(),
            socialType = socialType,
            user = user,
            email = socialAccount.email()
        )
        doReturn(socialUser).`when`(socialUserIdentificationRepository)
            .getBySocialTypeAndSocialIdentification(any(), anyString())
        doReturn(requestContext.userKey).`when`(user).id

        // * when
        service.integration(socialType, authorizationCode)
    }

    @Test
    fun `유저 통합 성공 (새로 통합하는 케이스)`() {
        // * given
        val socialType = SocialType.KAKAO
        val authorizationCode = "ofijaiowef.foaiwsejfoa.asodfijaweo"
        val socialAccount = SocialAccountImpl(
            id = "oklfjawo;eifj",
            email = "test@kakao.com",
        )
        doReturn(socialAccount).`when`(socialLoginApiService).loginAuthorization(eq(socialType), eq(authorizationCode))
        val user = Mockito.mock(User::class.java)
        val socialUser = SocialUserIdentificationFactory.create(
            socialIdentification = socialAccount.socialIdentification(),
            socialType = socialType,
            user = user,
            email = socialAccount.email()
        )
        doReturn(socialUser).`when`(socialUserIdentificationRepository)
            .getBySocialTypeAndSocialIdentification(any(), anyString())
        doReturn(2222L).`when`(user).id
        doNothing().`when`(nativeQueryRepository).integration(anyLong(), anyLong())

        // * when
        service.integration(socialType, authorizationCode)
    }

    @Test
    fun `유저 통합 실패 (알 수 없는 네티이브 쿼리 오류)`() {
        // * given
        val socialType = SocialType.KAKAO
        val authorizationCode = "ofijaiowef.foaiwsejfoa.asodfijaweo"
        val socialAccount = SocialAccountImpl(
            id = "oklfjawo;eifj",
            email = "test@kakao.com",
        )
        doReturn(socialAccount).`when`(socialLoginApiService).loginAuthorization(eq(socialType), eq(authorizationCode))
        val user = Mockito.mock(User::class.java)
        val socialUser = SocialUserIdentificationFactory.create(
            socialIdentification = socialAccount.socialIdentification(),
            socialType = socialType,
            user = user,
            email = socialAccount.email()
        )
        doReturn(socialUser).`when`(socialUserIdentificationRepository)
            .getBySocialTypeAndSocialIdentification(any(), anyString())
        doReturn(2222L).`when`(user).id
        doThrow(RuntimeException()).`when`(nativeQueryRepository).integration(anyLong(), anyLong())

        // * when
        val throwable = catchThrowable { service.integration(socialType, authorizationCode) }
        
        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.INTEGRATION_SQL_FAIL)
    }

    @Test
    fun `소셜 어카운트 목록조회`() {
        // * given
        val socialAccounts = listOf(
            SocialUserIdentificationFactory.create(
                socialIdentification = "1",
                socialType = SocialType.GOOGLE,
                user = Mockito.mock(User::class.java),
                email = "test@gmail.com"
            ),
            SocialUserIdentificationFactory.create(
                socialIdentification = "2",
                socialType = SocialType.KAKAO,
                user = Mockito.mock(User::class.java),
                email = "test@kakao.com"
            ),
            SocialUserIdentificationFactory.create(
                socialIdentification = "2",
                socialType = SocialType.EMAIL,
                user = Mockito.mock(User::class.java),
                email = "test@yahoo.jp"
            ),
        )
        doReturn(socialAccounts).`when`(socialUserIdentificationRepository).findAllByUserId(anyLong())

        // * when
        val result = service.retrieveIntegrationInfo()

        // * then
        assertThat(result.map { it.first }.toSet()).isEqualTo(socialAccounts.map { it.socialType }.toSet())
        assertThat(result.map { it.second }.toSet()).isEqualTo(socialAccounts.map { it.email }.toSet())
    }
}