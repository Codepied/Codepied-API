package com.codepied.api.api.mailing.dto

/**
 * interface to inject data in email template
 *
 * @author Aivyss
 * @since 2022/12/18
 */
interface EmailTemplateValues {
    fun values(): Map<String, String>
    fun to(): String
}

fun EmailTemplateValues.content(template: String): String {
    val values = this.values()

    var content = template
    values.entries.map { with(it) { content = template.replace("{{$key}}", value) } }

    return content
}