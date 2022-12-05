package com.codepied.api.api.locale

import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.http.SupportLanguage
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.LocaleResolver
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * LocaleResolver
 *
 * @author Aivyss
 * @since 12/04/2022
 */
@Component
class CustomLocaleResolver(
    val requestContext: RequestContext,
) : LocaleResolver {
    override fun resolveLocale(request: HttpServletRequest): Locale {
        return SupportLanguage.matches(languageId = request.getHeader("languageId"))
            .also {
                requestContext.supportLanguage = it
                LocaleContextHolder.setLocale(it.locale)
            }.locale
    }

    override fun setLocale(request: HttpServletRequest, response: HttpServletResponse?, locale: Locale?) {
        val languageHeader = locale?.toLanguageTag() ?: SupportLanguage.EN.locale.toLanguageTag()
        response?.setHeader("languageId", languageHeader)
        response?.setHeader("Accept-Language", languageHeader)
    }
}