package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("config.aws")
@ConstructorBinding
class AwsProperty(
    val accessKeyId: String,
    val secretAccessKey: String,
    val s3BucketName: String,
)