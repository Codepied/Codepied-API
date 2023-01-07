package com.codepied.api.api.security.application

import com.codepied.api.api.ObjectMapperHolder
import com.codepied.api.api.TimeService
import com.codepied.api.api.config.JwtProperty
import com.codepied.api.api.security.SocialType
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.test.MockStore
import com.codepied.api.test.MockStore.createOneUserDetails
import com.codepied.api.user.domain.UserDetailsRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mock
import org.mockito.kotlin.doReturn

class JwtServiceTest : AbstractServiceTest() {
    private val jwtProperty: JwtProperty = JwtProperty(
        accessTokenSecret = "access_secret",
        accessTokenLifetime = 10000,
        refreshTokenSecret = "refresh_secret",
        refreshTokenLifetime = 100000,
    )
    @Mock
    private lateinit var objectMapper: ObjectMapper
    @Mock
    private lateinit var userDetailsRepository: UserDetailsRepository

    private lateinit var service: JwtService

    @BeforeEach
    fun init() {
        this.objectMapper = ObjectMapperHolder.objectMapper
        this.service = JwtService(jwtProperty, TimeService(), objectMapper, userDetailsRepository)
    }

    @Test
    fun `엑세스 토큰 생성 성공`() {
        // * given
        val user = MockStore.createOneUser()

        // * when
        val token = service.generateAccessToken(user, SocialType.EMAIL)

        // * then
        assertThat(token.isNotBlank()).isTrue
    }

    @Test
    fun `리프레시 토큰 생성 성공`() {
        // * given
        val user = MockStore.createOneUser()

        // * when
        val token = service.generateRefreshToken(user, SocialType.EMAIL)

        // * then
        assertThat(token.isNotBlank()).isTrue
    }

    @Test
    fun `access token 파싱성공`() {
        // * given
        val user = MockStore.createOneUser()
        val accessToken = service.generateAccessToken(user, SocialType.EMAIL)

        // * when
        val principalDetails = service.parseAccessToken(accessToken)

        assertThat(principalDetails).isNotNull
        assertThat(principalDetails.userKey).isEqualTo(user.id)
        assertThat(principalDetails.roleTypes).isEqualTo(user.roles.map { it.roleType })
    }

    @Test
    fun `refresh token 파싱성공`() {
        // * given
        val user = MockStore.createOneUser()
        val refreshToken = service.generateRefreshToken(user, SocialType.EMAIL)

        // * when
        val loginInfo = service.parseRefreshToken(refreshToken)

        assertThat(loginInfo.userKey).isEqualTo(user.id)
        assertThat(loginInfo.socialType).isEqualTo(SocialType.EMAIL)
    }

    @Test
    fun `토큰 재발급 성공`() {
        // * given
        val userDetails = createOneUserDetails()
        val refreshToken = service.generateRefreshToken(userDetails.user, SocialType.EMAIL)
        doReturn(userDetails).`when`(userDetailsRepository).getByUserId(anyLong())

        // * when
        service.refreshTokens(refreshToken)
    }
}