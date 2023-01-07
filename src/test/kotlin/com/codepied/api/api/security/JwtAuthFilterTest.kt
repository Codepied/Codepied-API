package com.codepied.api.api.security

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.api.exception.ExceptionController
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.invalidRequest
import com.codepied.api.api.http.FailResponse
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.http.SupportLanguage
import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.application.JwtService
import com.codepied.api.api.security.dto.PrincipalDetails
import com.codepied.api.test.AbstractServiceTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import java.io.PrintWriter
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthFilterTest : AbstractServiceTest() {
    @Mock
    private lateinit var jwtService: JwtService
    @Mock
    private lateinit var exceptionController: ExceptionController
    @Mock
    private lateinit var requestContext: RequestContext
    @Mock
    private lateinit var objectMapper: ObjectMapper

    @InjectMocks
    private lateinit var filter: JwtAuthFilter

    @Test
    fun `토큰 파싱 성공 및 Authentication 생성 성공`() {
        // * given
        val request = Mockito.mock(HttpServletRequest::class.java)
        val response = Mockito.mock(HttpServletResponse::class.java)
        val filterChain = Mockito.mock(FilterChain::class.java)
        val accessToken = "test.token.ok"
        doReturn("Bearer $accessToken").`when`(request).getHeader(eq("Authorization"))
        val principalDetails = PrincipalDetails(
            userKey = 1L,
            roleTypes = listOf(RoleType.USER),
            socialType = SocialType.EMAIL,
        )
        doReturn(principalDetails).`when`(jwtService).parseAccessToken(eq(accessToken))
        doNothing().`when`(filterChain).doFilter(eq(request), eq(response))

        // * when
        filter.doFilter(request, response, filterChain)

        // * then
        val authentication = SecurityContextHolder.getContext().authentication
        assertThat(authentication.principal is PrincipalDetails).isTrue
        val actualPrincipal = authentication.principal as PrincipalDetails
        assertThat(actualPrincipal).isEqualTo(principalDetails)
    }

    @Test
    fun `토큰 파싱 실패 예외처리`() {
        val request = Mockito.mock(HttpServletRequest::class.java)
        val response = Mockito.mock(HttpServletResponse::class.java)
        val filterChain = Mockito.mock(FilterChain::class.java)
        val accessToken = "test.token.invalid"
        doReturn("Bearer $accessToken").`when`(request).getHeader(eq("Authorization"))
        doThrow(
            invalidRequest(
                errorCode = BusinessErrorCode.ACCESS_TOKEN_EXPIRED,
                debugMessage = "access token expired",
            )
        ).`when`(jwtService).parseAccessToken(eq(accessToken))
        val writer = Mockito.mock(PrintWriter::class.java)
        doReturn(writer).`when`(response).writer
        doNothing().`when`(writer).write(anyString())
        doReturn("{}").`when`(objectMapper).writeValueAsString(any())
        val failResponse = FailResponse(
            errorCode = BusinessErrorCode.ACCESS_TOKEN_EXPIRED.getCodeValue(),
            interfaceMessage = "test",
            supportLanguage = SupportLanguage.KO,
            responseTime = LocalDateTime.now(),
            httpStatus = HttpStatus.BAD_REQUEST
        )
        doReturn(failResponse).`when`(exceptionController).invalidRequestException(any<InvalidRequestException>())

        filter.doFilter(request, response, filterChain)
    }
}