package com.codepied.api.api.http

import java.util.*

/**
 * Supported Languages
 *
 * @author Aivyss
 * @since 11/30/2022
 */
enum class SupportLanguage(val locale: Locale, val languageId: String, val aliases: List<String>) {
    KO(Locale.forLanguageTag("ko"), "ko", listOf("kr", "korea")),
    EN(Locale.forLanguageTag("en"), "en", listOf("us", "gb")),
    ;

    companion object {
        private val observer = values().associateBy { it.languageId.uppercase() }

        fun matches(languageId: String?): SupportLanguage = observer[languageId?.uppercase()] ?: KO
    }
}