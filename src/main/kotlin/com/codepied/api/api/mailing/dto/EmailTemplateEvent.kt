package com.codepied.api.api.mailing.dto

/**
 * interface to inject data in email template
 *
 * @author Aivyss
 * @since 2022/12/18
 */
interface EmailTemplateEvent {
    fun values(): Map<String, String>
    fun to(): String
    fun content(template: String): String {
        val values = this.values()

        var content = template
        values.entries.map { with(it) { content = content.replace("{{$key}}", value) } }

        return content
    }
}