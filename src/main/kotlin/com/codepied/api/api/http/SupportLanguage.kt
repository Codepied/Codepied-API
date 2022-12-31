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
//    JP(Locale.forLanguageTag("ja"), "ja", listOf("jp", "japan")),
    ;

    companion object {
        fun matches(languageId: String?): SupportLanguage {
            return try {
                SupportLanguage.valueOf(languageId?.uppercase() ?: KO.name)
            } catch (e: Exception) {
                SupportLanguage.values().firstOrNull {
                    it.languageId.uppercase() == languageId?.uppercase()
                            || languageId in it.aliases
                } ?: KO
            }
        }
    }
}