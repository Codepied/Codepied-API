package com.codepied.api.user.application

import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.dto.UserDataDuplicateType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq

class UserInfoServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var socialUserIdentificationRepository: SocialUserIdentificationRepository

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
}