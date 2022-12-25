package com.codepied.api.user.application

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestException
import com.codepied.api.api.http.RequestContext
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.test.MockStore.createOneUserCredential
import com.codepied.api.test.MockStore.createOneUserDetails
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.domain.UserCredentialRepository
import com.codepied.api.user.domain.UserDetailsRepository
import com.codepied.api.user.dto.UserDataDuplicateType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.security.crypto.password.PasswordEncoder

class UserInfoServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var socialUserIdentificationRepository: SocialUserIdentificationRepository
    @Mock
    private lateinit var userCredentialRepository: UserCredentialRepository
    @Mock
    private lateinit var requestContext: RequestContext
    @Mock
    private lateinit var passwordEncoder: PasswordEncoder
    @Mock
    private lateinit var userDetailsRepository: UserDetailsRepository

    @InjectMocks
    private lateinit var service: UserInfoService

    @Test
    fun `이메일 중복체크 성공`() {
        // * given
        val data = "test@test.com"
        val type = UserDataDuplicateType.EMAIL

        doReturn(true).`when`(socialUserIdentificationRepository).existsByEmail(eq(data))

        // * when
        val result = service.checkDuplicatedUserInfo(data, type)

        // * then
        assertThat(result).isTrue
    }

    @Test
    fun `비밀번호 변경 성공`() {
        // * given
        val userCredential = createOneUserCredential()
        doReturn(userCredential).`when`(userCredentialRepository).findByUserId(anyLong())
        doReturn(true).`when`(passwordEncoder).matches(anyString(), anyString())
        doReturn("new password").`when`(passwordEncoder).encode(anyString())

        // * when
        service.changePassword("new password", "old password")
    }

    @Test
    fun `비밀번호 변경 실패 - 일치하는 유저가 없음`() {
        // * given
        doReturn(null).`when`(userCredentialRepository).findByUserId(anyLong())

        // * when
        val throwable = catchThrowable { service.changePassword("new password", "old") }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NO_SUCH_USER_LOGIN)
    }

    @Test
    fun `비밀번호 변경 실패 - 예전 비밀번호 불일치`() {
        // * given
        val userCredential = createOneUserCredential()
        doReturn(userCredential).`when`(userCredentialRepository).findByUserId(anyLong())
        doReturn(false).`when`(passwordEncoder).matches(anyString(), anyString())

        // * when
        val throwable = catchThrowable { service.changePassword("new password", "old") }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NOT_MATCHES_PASSWORD_LOGIN_ERROR)
    }

    @Test
    fun `닉네임 변경 성공`() {
        // * given
        val userDetails = createOneUserDetails()
        doReturn(userDetails).`when`(userDetailsRepository).findByUserId(anyLong())

        // * when
        service.changeNickname("changeNickname")
    }

    @Test
    fun `닉네임 변경 실패`() {
        // * given
        doReturn(null).`when`(userDetailsRepository).findByUserId(anyLong())

        // * when
        val throwable = catchThrowable { service.changeNickname("nickname") }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(ErrorCode.NO_SUCH_USER_LOGIN)
    }
}