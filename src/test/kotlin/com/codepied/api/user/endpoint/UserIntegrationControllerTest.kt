package com.codepied.api.user.endpoint

import com.codepied.api.api.security.SocialType
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


internal class UserIntegrationControllerTest : AbstractUserEndpointTest("/api/users/integration") {

    @Test
    fun `유저 통합 성공 (EMAIl TO SOCIAL)`() {
        // * given
        doNothing().`when`(userIntegrationService).integrationEmailToSocial(any())

        // * when
        val perform = mockMvc.perform(
            patch(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $accessToken")
                .content(
                    """{
                    "email": "test@gmail.com",
                    "password" : "testpassword1234"
                }""".trimIndent()))

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(document(
                DocumentEnum.EMAIL_USER_INTEGRATION.name,
                HeaderDocumentation.requestHeaders(
                    HeaderDocumentation.headerWithName("Authorization").description("Bearer \${accessToken}"),
                ),
                PayloadDocumentation.requestFields(
                    fieldWithPath("email").type("email").description("email"),
                    fieldWithPath("password").type("password").description("password")
                ),
                RestDocStore.responseSnippet(
                    fieldWithPath("data").type("Boolean").description("success!!"),
                )
            ))
    }

    @Test
    fun `유저 통합 성공 (SOCIAL TO SOCIAL, EMAIL)`() {
        // * given
        doNothing().`when`(userIntegrationService).integrationSocialToAny(any(), anyString())

        // * when
        val perform = mockMvc.perform(
            put(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $accessToken")
                .content(
                """{
                    "socialType": "KAKAO",
                    "authorizationCode" : "fjoefiasd,mzfsafaweasdf.asdfkl"
                }""".trimIndent()))

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(document(
                DocumentEnum.USER_INTEGRATION.name,
                HeaderDocumentation.requestHeaders(
                    HeaderDocumentation.headerWithName("Authorization").description("Bearer \${accessToken}"),
                ),
                PayloadDocumentation.requestFields(
                    fieldWithPath("socialType").type("SocialType").description("social type"),
                    fieldWithPath("authorizationCode").type("String").description("social authorization code")
                ),
                RestDocStore.responseSnippet(
                    fieldWithPath("data").type("Boolean").description("success!!"),
                )
            ))
    }

    @Test
    fun `연동된 소셜 유저 조회`() {
        // * given
        val socialTypes = listOf(
            SocialType.KAKAO to "test@kakao.com",
            SocialType.GOOGLE to "test@gmail.com",
        )
        doReturn(socialTypes).`when`(userIntegrationService).retrieveIntegrationInfo()

        // * when
        val perform = mockMvc.perform(
            get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $accessToken")
        )

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andDo(document(
                DocumentEnum.RETRIEVE_USER_INTEGRATIONS.name,
                HeaderDocumentation.requestHeaders(
                    HeaderDocumentation.headerWithName("Authorization").description("Bearer \${accessToken}"),
                ),
                RestDocStore.responseSnippet(
                    fieldWithPath("data").type("List<Pair<SocialType, String?>>").description("success!!"),
                    fieldWithPath("data[].first").type("SocialType").description("social type"),
                    fieldWithPath("data[].second").type("String?").description("email"),
                )
            ))
    }
}