package com.codepied.api.api.config

import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.http.SupportLanguage
import com.codepied.api.api.locale.CustomLocaleResolver
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver

/**
 * 1. message source configuration
 * 2. LocaleResolver configuration
 *
 * @author Aivyss
 * @since 11/30/2022
 */
@Configuration
class I18nConfig {
    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasenames("classpath:/messages/message")
        messageSource.setDefaultLocale(SupportLanguage.EN.locale)
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setCacheSeconds(60)

        return messageSource
    }

    @Bean
    fun localeResolver(requestContext: RequestContext): LocaleResolver = CustomLocaleResolver(requestContext = requestContext)
}