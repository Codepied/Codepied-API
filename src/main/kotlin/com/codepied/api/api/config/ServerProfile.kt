package com.codepied.api.api.config

import com.codepied.api.api.exception.ServerExceptionBuilder.throwCodeError
import com.codepied.api.api.exception.ServerExceptionBuilder.throwInternalServerError

/**
 * Server Profile
 * @author Aivyss
 * @since 2023/01/02
 */
enum class ServerProfile {
    SVC,
    DEV,
    TEST,
    ;

    companion object {
        fun matches(code: String): ServerProfile {
            val uppercase = code.uppercase()
            return ServerProfile.values().firstOrNull { it.name == uppercase } ?: throwCodeError()
        }
    }
}