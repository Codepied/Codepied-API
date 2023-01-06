package com.codepied.api.user.endpoint

import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


internal class UserIntegrationControllerTest : AbstractUserEndpointTest("/api/users/integration") {
    @Test
    fun `유저 통합 성공`() {
        // * given
        doNothing().`when`(userIntegrationService).integration(any(), anyString())

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
}