package com.codepied.api.user.endpoint

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import com.codepied.api.user.dto.UserDataDuplicateType
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class UserInfoOpenApiControllerTest : AbstractUserEndpointTest("/open-api/users/info") {
    @Test
    fun `중복체크 성공`() {
        // * given
        val email = "test@test.com"
        doReturn(true).`when`(userInfoService).checkDuplicatedUserInfo(eq(email), eq(UserDataDuplicateType.EMAIL))

        // * when
        val perform = mockMvc.perform(
            MockMvcRequestBuilders.get("$uri/duplicate")
                .param("type", "EMAIL")
                .param("data", email)
        )

        // * then
        perform.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true))
            .andDo(
                MockMvcRestDocumentation.document(
                    DocumentEnum.USER_INFO_DUPLICATE.name, RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("type").description("EMAIL, NICKNAME"),
                        RequestDocumentation.parameterWithName("data").description("검사할 데이터")
                    ), RestDocStore.responseSnippet(
                        PayloadDocumentation.fieldWithPath("data").type("Boolean").description("중복여부")
                    )
                )
            )
    }

    @Test
    fun `중복체크 실패 - 적합하지 않은 타입`() {
        // * when
        val perform = mockMvc.perform(
            MockMvcRequestBuilders.get("$uri/duplicate")
                .param("type", "INVALID")
                .param("data", "test@test.com")
        )

        // * then
        perform.andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode").value(BusinessErrorCode.INVALID_ENUM_CODE_ERROR.name))
    }

    @Test
    fun test() {
        val arr = "/open-api/file/fieo/adfke/fdaksd".split("/")
        println(arr[1])
    }
}