package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("config.async")
@ConstructorBinding
class AsyncProperty(
    val asyncPoolMinCapacity: Int,
    val asyncPoolMaxCapacity: Int,
    val asyncPoolQueueCapacity: Int,
)