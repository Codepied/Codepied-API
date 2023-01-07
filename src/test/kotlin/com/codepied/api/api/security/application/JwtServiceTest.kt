package com.codepied.api.api.security.application

import com.codepied.api.api.ObjectMapperHolder
import com.codepied.api.api.TimeService
import com.codepied.api.api.config.JwtProperty
import com.codepied.api.api.security.SocialType
import com.codepied.api.test.AbstractServiceTest
import com.codepied.api.test.MockStore
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock

class JwtServiceTest : AbstractServiceTest() {
    private val jwtProperty: JwtProperty = JwtProperty(
        accessTokenSecret = "access_secret",
        accessTokenLifetime = 10000,
        refreshTokenSecret = "refresh_secret",
        refreshTokenLifetime = 100000,
    )
    @Mock
    private lateinit var objectMapper: ObjectMapper

    private lateinit var service: JwtService

    @BeforeEach
    fun init() {
        this.objectMapper = ObjectMapperHolder.objectMapper
        this.service = JwtService(jwtProperty, TimeService(), objectMapper)
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
        val token = service.generateRefreshToken(user)

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
}