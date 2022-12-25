package com.codepied.api.user.dto

import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwEnumCodeError

/**
 * User Unique data types
 *
 * @author Aivyss
 * @since 2022/12/25
 */
enum class UserDataDuplicateType {
    EMAIL,
    NICKNAME,
    ;

    companion object {
        fun matches(data: String): UserDataDuplicateType = UserDataDuplicateType.values()
            .firstOrNull { it.name == data } ?: throwEnumCodeError()
    }
}