package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("config.social-login.naver")
@ConstructorBinding
class NaverSocialLoginProperty(
    val loginUri: String,
    val requestTokenUri: String,
    val clientId: String,
    val clientSecret: String,
)