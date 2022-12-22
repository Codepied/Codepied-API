package com.codepied.api.user.endpoint

import com.codepied.api.api.security.EmailLoginService
import com.codepied.api.api.security.LoginInfoImpl
import com.codepied.api.api.security.SocialLoginService
import com.codepied.api.test.AbstractEndpointTest
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.kotlin.any
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserAuthControllerTest : AbstractEndpointTest("/api/users/auths") {
    @Mock
    private lateinit var socialLoginService: SocialLoginService

    @Mock
    private lateinit var emailLoginService: EmailLoginService

    @InjectMocks
    private lateinit var controller: UserAuthController

    @Test
    fun `이메일 회원가입 성공`() {
        // * given
        doReturn(LoginInfoImpl(
            userKey = 1L,
            accessToken = "",
            refreshToken = "",
            nickname = "test_nickname",
            userProfile = null,
            email = "test@test.com"
        )).`when`(emailLoginService).signup(any())

        // * when
        val perform = mockMvc(controller).perform(post("$uri/signup")
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
                    fieldWithPath("data").type("EmailUserCreate").description("user email"),
                    fieldWithPath("data.userKey").type("Long").description("userKey"),
                    fieldWithPath("data.accessToken").type("String").description("JWT, but empty"),
                    fieldWithPath("data.refreshToken").type("String").description("JWT, but empty"),
                    fieldWithPath("data.nickname").type("String").description("user nickname"),
                    fieldWithPath("data.userProfile").type("String?").description("user profile").optional(),
                    fieldWithPath("data.email").type("String").description("user email"),
            )))
    }
}