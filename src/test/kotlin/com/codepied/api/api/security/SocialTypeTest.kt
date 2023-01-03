package com.codepied.api.api.security

import com.codepied.api.api.exception.CodepiedBaseException.ServerException
import com.codepied.api.api.exception.ServerErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

internal class SocialTypeTest {
    @Test
    fun `matchs 함수 테스트 - 성공`() {
        val socialType = SocialType.matches("KAKAO")

        assertThat(socialType).isEqualTo(SocialType.KAKAO)
    }

    @Test
    fun `matches 함수 테스트 - 실패`() {
        val throwable = catchThrowable { SocialType.matches("INVALID") }

        assertThat(throwable is ServerException).isTrue
        val exception = throwable as ServerException
        assertThat(exception.errorCode).isEqualTo(ServerErrorCode.CODE_ERROR)
    }

    @Test
    fun test() {
    }
}