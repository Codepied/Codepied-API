package com.codepied.api.api.exception

import com.codepied.api.api.CodeEnum

/**
 * i18n localeMessage Code based on exception for invalid data processing
 *
 * @author Aivyss
 * @since 11/30/2022
 */
enum class ErrorCode : CodeEnum {
    INTERNAL_SERVER_ERROR,
    NO_RESOURCE_ERROR,
    SIGN_UP_DUPLICATE_EMAIL_ERROR,
    INVALID_ACCESS_USER,
    NO_SUCH_USER_LOGIN_ERROR,
    NOT_MATCHES_PASSWORD_LOGIN_ERROR,
    ACCESS_TOKEN_EXPIRED,
    INVALID_ACCESS_TOKEN,
    DUPLICATED_EMAIL_SIGNUP,
    DUPLICATED_NICKNAME,
    NOT_AUTHORIZED_EMAIL_USER,
    NOT_ACCESSIBLE_SOCIAL_USER_GOOGLE,
    NOT_ACCESSIBLE_SOCIAL_USER_NAVER,
    NOT_ACCESSIBLE_SOCIAL_USER_KAKAO,
    INVALID_ENUM_CODE_ERROR,
    ;

    override fun getCodeValue(): String = "EXCEPTION.$name"
    override fun getNameValue(): String = this.name

    companion object {
        fun matches(str: String?): CodeEnum = ErrorCode.values().find { it.getCodeValue() == str } ?: INTERNAL_SERVER_ERROR
    }
}

/**
 * i18n localeMessage Code based on validation fail exception for validation of request body
 *
 *  @author Aivyss
 *  @since 11/30/2022
 */
enum class ParameterErrorCode : CodeEnum {
    EMAIL_FORMAT,
    ;

    override fun getCodeValue(): String = "EXCEPTION.PARAMETERS.${name}"
    override fun getNameValue(): String = this.name

    companion object {
        fun matches(str: String?): CodeEnum = ParameterErrorCode.values().find { it.getCodeValue() == str } ?: ErrorCode.INTERNAL_SERVER_ERROR
    }
}
