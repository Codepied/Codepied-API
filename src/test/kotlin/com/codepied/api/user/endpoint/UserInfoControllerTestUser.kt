package com.codepied.api.user.endpoint

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import com.codepied.api.user.dto.UserDataDuplicateType
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserInfoControllerTestUser : AbstractUserEndpointTest("/api/users/info") {
    @Test
    fun `중복체크 성공`() {
        // * given
        val email = "test@test.com"
        doReturn(true).`when`(userInfoService).checkDuplicatedUserInfo(eq(email), eq(UserDataDuplicateType.EMAIL))

        // * when
        val perform = mockMvc.perform(
            get("$uri/duplicate")
                .param("type", "EMAIL")
                .param("data", email)
        )

        // * then
        perform.andExpect(status().isOk)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(document(DocumentEnum.USER_INFO_DUPLICATE.name, requestParameters(
                parameterWithName("type").description("EMAIL, NICKNAME"),
                parameterWithName("data").description("검사할 데이터")
            ), RestDocStore.responseSnippet(
                fieldWithPath("data").type("Boolean").description("중복여부")
            )))
    }

    @Test
    fun `적합하지 않은 타입`() {
        // * when
        val perform = mockMvc.perform(
            get("$uri/duplicate")
                .param("type", "INVALID")
                .param("data", "test@test.com")
        )

        // * then
        perform.andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_ENUM_CODE_ERROR.name))
    }
}