package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("config.social-login.google")
@ConstructorBinding
class GoogleSocialLoginProperty(val loginUri: String)