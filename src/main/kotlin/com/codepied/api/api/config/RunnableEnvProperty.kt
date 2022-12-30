package com.codepied.api.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Runnable Environment Properties
 * most cases: svc=true, test=false
 *
 * @author Aivyss
 * @since 2022/12/31
 */
@ConfigurationProperties("runnable-env")
@ConstructorBinding
class RunnableEnvProperty(
    val signupAuthorizationEmail: Boolean,
)