package com.codepied.api.test

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

/**
 * Spring Rest Doc 적용 및 standalone Setup을 위한 추상클래스
 *
 * @author Aivyss
 * @since 2022/12/22
 */
@ActiveProfiles("test")
@ExtendWith(value = [RestDocumentationExtension::class, MockitoExtension::class])
abstract class AbstractEndpointTest(val uri: String) {

    private lateinit var restDocumentation: RestDocumentationContextProvider

    @BeforeEach
    fun initRestDocumentation(restDocumentation: RestDocumentationContextProvider) {
        this.restDocumentation = restDocumentation
    }

    fun mockMvc(controller: Any) = MockMvcBuilders
            .standaloneSetup(controller)
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
}