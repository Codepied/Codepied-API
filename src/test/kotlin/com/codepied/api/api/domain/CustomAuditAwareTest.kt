package com.codepied.api.api.domain

import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.CustomAuthentication
import com.codepied.api.api.security.dto.PrincipalDetails
import com.codepied.api.test.AbstractServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.springframework.security.core.context.SecurityContextHolder

class CustomAuditAwareTest : AbstractServiceTest() {
    @InjectMocks
    private lateinit var aware: CustomAuditAware

    @Test
    fun `user key 추출 성공 - not null`() {
        // * given
        val authentication = CustomAuthentication(
            principal = PrincipalDetails(
                userKey = 1L,
                roleTypes = listOf(RoleType.USER),
                socialType = SocialType.EMAIL,
            ),
            authenticated = true
        )
        SecurityContextHolder.getContext().authentication = authentication

        // * when
        val userKeyOptional = aware.currentAuditor

        // * then
        assertThat(userKeyOptional.isPresent).isTrue
        assertThat(userKeyOptional.get()).isEqualTo(authentication.details.userKey)
    }

    @Test
    fun `user key 추출 성공 - null`() {
        // * when
        val userKeyOptional = aware.currentAuditor

        // * then
        assertThat(userKeyOptional.isPresent).isFalse
    }
}