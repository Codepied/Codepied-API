package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * application loading을 수행하는 클래스에 @ConfigurationPropertiesScan 어노테이션 적용 및
 * annotationr processor "org.springframework.boot:spring-boot-configuration-processor" 적용 필요
 *
 * @author Aivyss
 * @since 11/30/2022
 */
@ConfigurationProperties("domain.allow-cors")
@ConstructorBinding
class CorsDomainProperty(
    val localhost: String,
)