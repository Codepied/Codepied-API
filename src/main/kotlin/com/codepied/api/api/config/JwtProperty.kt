package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("jwt")
@ConstructorBinding
class JwtProperty(
    val accessTokenSecret: String,
    val refreshTokenSecret: String,
    val accessTokenLifetime: Long,
    val refreshTokenLifetime: Long,
)