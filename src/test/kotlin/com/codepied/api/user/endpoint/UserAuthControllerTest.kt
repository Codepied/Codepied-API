package com.codepied.api.user.endpoint

import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.EmailLoginInfoImpl
import com.codepied.api.api.security.dto.LoginInfoImpl
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserAuthControllerTest : AbstractUserEndpointTest("/api/users/auths") {
    @Test
    fun `이메일 회원가입 성공`() {
        // * given
        doReturn(
            LoginInfoImpl(
                userKey = 1L,
                accessToken = "",
                refreshToken = "",
                nickname = "test_nickname",
                userProfile = null,
                email = "test@test.com"
        )
        ).`when`(emailLoginService).signup(any())

        // * when
        val perform = mockMvc.perform(post("$uri/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""{
                    "email": "test@test.com",
                    "password": "password_test",
                    "nickname": "test_nickname"
                }""".trimIndent()
                )
        )

        val test: List<FieldDescriptor> = listOf<FieldDescriptor>()
        requestFields(test)
        // * then
        perform.andExpect(status().isCreated)
                .andExpect(jsonPath("$.data.userKey").isNumber)
                .andDo(document(DocumentEnum.EMAIL_SIGNUP.name, requestFields(
                    fieldWithPath("email").type("String").description("user email"),
                    fieldWithPath("password").type("String").description("user password"),
                    fieldWithPath("nickname").type("String").description("user nickname"),
                ), RestDocStore.responseSnippet(
                    fieldWithPath("data").type("LoginInfo").description("login information"),
                    fieldWithPath("data.userKey").type("Long").description("userKey"),
                    fieldWithPath("data.accessToken").type("String").description("JWT, but empty"),
                    fieldWithPath("data.refreshToken").type("String").description("JWT, but empty"),
                    fieldWithPath("data.nickname").type("String").description("user nickname"),
                    fieldWithPath("data.userProfile").type("String?").description("user profile").optional(),
                    fieldWithPath("data.email").type("String").description("user email"),
                )))
    }

    @Test
    fun `이메일 로그인 성공`() {
        // * given
        doReturn(
            EmailLoginInfoImpl(
                userKey = 1L,
                accessToken = "oghoqr320jftg[0awifdjfoiemacvzkz.fiowefma.zdfiefas",
                refreshToken = "o323mf2iofa.foi3jfm;a.ifjawoeif",
                nickname = "test_nickname",
                userProfile = null,
                email = "test@test.com",
                passwordChangeRecommended = true,
        )).`when`(emailLoginService).login(any())

        // * when
        val perform = mockMvc.perform(post("$uri/login")
            .param("type", "EMAIL")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                """{
                "email": "test@test.com",
                "password": "password_test"
            }""".trimIndent()))

        // * then
        perform.andExpect(status().isOk)
                .andExpect(jsonPath("$.data.userKey").isNumber)
                .andDo(
                    document(DocumentEnum.EMAIL_LOGIN.name, requestFields(
                        fieldWithPath("email").type("String").description("user email"),
                        fieldWithPath("password").type("String").description("user password"),
                    ), RestDocStore.responseSnippet(
                        fieldWithPath("data").type("LoginInfo").description("login information"),
                        fieldWithPath("data.userKey").type("Long").description("userKey"),
                        fieldWithPath("data.accessToken").type("String").description("access JWT"),
                        fieldWithPath("data.refreshToken").type("String").description("refresh JWT"),
                        fieldWithPath("data.nickname").type("String").description("user nickname"),
                        fieldWithPath("data.userProfile").type("String?").description("user profile").optional(),
                        fieldWithPath("data.email").type("String").description("user email"),
                        fieldWithPath("data.passwordChangeRecommended").type("Boolean").description("패스워드 변경 권장여부"),
                    )),
                )
    }

    @Test
    fun `소셜 로그인 성공`() {
        // * given
        val authorizationCode = "test_authorization_code"
        val socialType = SocialType.GOOGLE
        val responseData = LoginInfoImpl(
            userKey = 1L,
            accessToken = "oghoqr320jftg[0awifdjfoiemacvzkz.fiowefma.zdfiefas",
            refreshToken = "o323mf2iofa.foi3jfm;a.ifjawoeif",
            nickname = "test_nickname",
            userProfile = null,
            email = "test@gmail.com"
        )
        doReturn(responseData).`when`(socialLoginService).login(eq(socialType), eq(authorizationCode))

        // * when
        val perform = mockMvc.perform(post("$uri/login")
            .param("type", socialType.name)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content("""{ "authorizationCode": "$authorizationCode" }""".trimIndent()))

        perform.andExpect(status().isOk)
            .andExpect(jsonPath("$.data.accessToken").value(responseData.getAccessToken()))
            .andDo(document(
                DocumentEnum.SOCIAL_LOGIN.name,
                requestFields(
                    fieldWithPath("authorizationCode").type("String").description("social login authorization code")
                ),
                RestDocStore.responseSnippet(
                    fieldWithPath("data").type("LoginInfo").description("login information"),
                    fieldWithPath("data.userKey").type("Long").description("userKey"),
                    fieldWithPath("data.accessToken").type("String").description("access JWT"),
                    fieldWithPath("data.refreshToken").type("String").description("refresh JWT"),
                    fieldWithPath("data.nickname").type("String").description("user nickname"),
                    fieldWithPath("data.userProfile").type("String?").description("user profile").optional(),
                    fieldWithPath("data.email").type("String").description("user email"),
                ))
            )
    }
}