package com.codepied.api.api.mailing.dto

/**
 * template values for email-signup authorization
 *
 * @author Aivyss
 * @since 2022/12/18
 */
data class EmailSignupAuthorizationValues(
    val to: String,
    val uri: String,
) : EmailTemplateValues {
    override fun values(): Map<String, String> = mapOf("uri" to uri)

    override fun to(): String = to
}