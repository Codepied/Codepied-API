package com.codepied.api.api.http

import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RequestContextTest {

    @Test
    fun `late init test`() {
        val requestContext = RequestContext().apply {
            this.userKey = 25
            this.roleTypes = listOf(RoleType.USER)
            this.supportLanguage = SupportLanguage.KO
            this.socialType = SocialType.KAKAO
            this.validUser = true
        }

        assertThat(requestContext.socialType).isEqualTo(SocialType.KAKAO)
    }
}