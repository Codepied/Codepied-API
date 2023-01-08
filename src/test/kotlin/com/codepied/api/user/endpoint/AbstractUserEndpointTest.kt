package com.codepied.api.user.endpoint

import com.codepied.api.api.config.I18nConfig
import com.codepied.api.api.exception.ExceptionController
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.locale.LocaleChangeFilter
import com.codepied.api.api.security.JwtAuthFilter
import com.codepied.api.api.security.application.EmailLoginService
import com.codepied.api.api.security.application.JwtService
import com.codepied.api.api.security.application.SocialLoginService
import com.codepied.api.user.application.UserInfoService
import com.codepied.api.user.application.UserIntegrationService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Spring Rest Doc 적용 및 mockMvc Setup을 위한 추상클래스
 *
 * @author Aivyss
 * @since 2022/12/22
 */
@ActiveProfiles("test")
@ImportAutoConfiguration(MessageSourceAutoConfiguration::class)
@MockBean(JpaMetamodelMappingContext::class)
@ExtendWith(value = [RestDocumentationExtension::class, SpringExtension::class])
@WebMvcTest(
    value = [
        ExceptionController::class,
        UserInfoOpenApiController::class,
        UserInfoController::class,
        UserAuthController::class,
        UserIntegrationController::class,
        RequestContext::class,
        MockMvcRestDocumentationConfigurer::class,
        RestDocumentationContextProvider::class,
        I18nConfig::class,
    ],
    excludeFilters =[ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [LocaleChangeFilter::class, JwtAuthFilter::class])],
    excludeAutoConfiguration = [
        SecurityAutoConfiguration::class,
    ]
)
abstract class AbstractUserEndpointTest(val uri: String) {
    @MockBean
    protected lateinit var userInfoService: UserInfoService
    @MockBean
    protected lateinit var socialLoginService: SocialLoginService
    @MockBean
    protected lateinit var emailLoginService: EmailLoginService
    @MockBean
    protected lateinit var userIntegrationService: UserIntegrationService
    @MockBean
    protected lateinit var jwtService: JwtService

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var restDocumentation: RestDocumentationContextProvider

    protected lateinit var mockMvc: MockMvc

    protected val accessToken = "fjoaiwefasw.fkjasfoawef.fjioaefjawe"
    protected val refreshToken = "fiawjeofaswesf.viaowefjwe.ckZXJCOasd"

    @BeforeEach
    fun initRestDocumentation(restDocumentation: RestDocumentationContextProvider) {
        this.restDocumentation = restDocumentation
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }
}