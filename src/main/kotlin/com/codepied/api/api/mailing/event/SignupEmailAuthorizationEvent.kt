package com.codepied.api.api.mailing.event

import com.codepied.api.api.mailing.dto.EmailTemplateEvent

class SignupEmailAuthorizationEvent(
    val to: String,
    val uri: String,
) : EmailTemplateEvent {
    override fun values(): Map<String, String> = mapOf("uri" to uri)

    override fun to(): String = to
}