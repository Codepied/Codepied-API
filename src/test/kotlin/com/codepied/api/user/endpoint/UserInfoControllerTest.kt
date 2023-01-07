package com.codepied.api.user.endpoint

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.test.DocumentEnum
import com.codepied.api.test.RestDocStore
import com.codepied.api.user.dto.UserDataDuplicateType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserInfoControllerTest : AbstractUserEndpointTest("/api/users/info") {
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
    fun `중복체크 실패 - 적합하지 않은 타입`() {
        // * when
        val perform = mockMvc.perform(
            get("$uri/duplicate")
                .param("type", "INVALID")
                .param("data", "test@test.com")
        )

        // * then
        perform.andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$.errorCode").value(BusinessErrorCode.INVALID_ENUM_CODE_ERROR.name))
    }

    @Test
    fun `비밀번호 변경 성공`() {
        // * given
        val oldPassword = "fjaoiwejfa"
        val newPassword = "$%fmia291gc"
        doNothing().`when`(userInfoService).changePassword(newPassword, oldPassword)

        // * when
        val perform = mockMvc.perform(
            patch(uri)
                .param("type", "PASSWORD")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "oldPassword": "$oldPassword",
                        "newPassword": "$newPassword"
                    }
                """.trimIndent())
        )

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(
                document(DocumentEnum.EMAIL_USER_PASSWORD_CHANGE.name,
                requestHeaders(
                    headerWithName("Authorization").description("Bearer \${accessToken}"),
                ),
                requestParameters(parameterWithName("type").description("PASSWORD (only)")),
                requestFields(
                    fieldWithPath("oldPassword").type("String").description("예전 비밀번호 / Not Blank 제약조건"),
                    fieldWithPath("newPassword").type("String").description("새로운 비밀번호 / 8 ~ 15자 특문 영문 숫자 포함")
                ),
                RestDocStore.responseSnippet(
                    fieldWithPath("data").type("Boolean").description("요청이 잘못되지 않을 경우 반드시 true")
                )
            ))
    }

    @Test
    fun `닉네임 변경 성공`() {
        // * given
        doNothing().`when`(userInfoService).changeNickname(anyString())

        // * when
        val perform = mockMvc.perform(
            patch(uri)
                .header("Authorization", "Bearer $accessToken")
                .param("type", "NICKNAME")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "nickname": "nickname"
                    }
                """.trimMargin()))

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(
                document(DocumentEnum.NICKNAME_CHANGE.name,
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer \${accessToken}"),
                    ),
                    requestParameters(parameterWithName("type").description("NICKNAME (only)")),
                    requestFields(
                        fieldWithPath("nickname").type("String").description("새 닉네임 / 공백불가 / 2 ~ 15자, 한글, 영문, 숫자만 가능"),
                    ),
                    RestDocStore.responseSnippet(
                        fieldWithPath("data").type("Boolean").description("요청이 잘못되지 않을 경우 반드시 true")
                    )
                ))
    }

    @Test
    fun `닉네임 변경 실패 - 적합하지 않은 닉네임 포맷`() {
        // * when
        val perform = mockMvc.perform(
            patch(uri)
                .param("type", "NICKNAME")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""{
                        "nickname": "new nickname"
                    }""".trimMargin()))

        // * then
        perform.andExpect(status().is4xxClientError)
            .andExpect(jsonPath("$.errorCode").value("INVALID_NICKNAME"))
    }

    @Test
    fun `프로파일 이미지 변경 성공`() {
        // * given
        doNothing().`when`(userInfoService).changeNickname(anyString())

        // * when
        val perform = mockMvc.perform(
            patch(uri)
                .header("Authorization", "Bearer $accessToken")
                .param("type", "PROFILE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "fileId": "fileId"
                    }
                """.trimIndent()))

        // * then
        perform.andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.data").value(true))
            .andDo(
                document(DocumentEnum.CHANGE_PROFILE.name,
                    requestHeaders(
                        headerWithName("Authorization").description("Bearer \${accessToken}"),
                    ),
                    requestParameters(parameterWithName("type").description("PROFILE (only)")),
                    requestFields(
                        fieldWithPath("fileId").type("String?").description("파일 아이디"),
                    ),
                    RestDocStore.responseSnippet(
                        fieldWithPath("data").type("Boolean").description("요청이 잘못되지 않을 경우 반드시 true")
                    )
                ))
    }
}