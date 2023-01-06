package com.codepied.api.api.exception

import com.codepied.api.api.CodeEnum

/**
 * i18n localeMessage Code based on exception for invalid data processing
 *
 * @author Aivyss
 * @since 11/30/2022
 */
enum class BusinessErrorCode : CodeEnum {
    UNKNOWN_ERROR,
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
    NO_SUCH_USER_LOGIN,
    EXCEED_FILE_SIZE,
    FAIL_TO_FILE_UPLOAD,
    INTEGRATION_SQL_FAIL,
    ;

    override fun getCodeValue(): String = "EXCEPTION.$name"

    companion object {
        fun matches(str: String?): CodeEnum = BusinessErrorCode.values().find { it.getCodeValue() == str } ?: ServerErrorCode.INTERNAL_SERVER_ERROR
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
    NOT_BLANK,
    NICKNAME_SIZE,
    INVALID_NICKNAME,
    ;

    override fun getCodeValue(): String = "EXCEPTION.PARAMETERS.${name}"

    companion object {
        fun matches(str: String?): CodeEnum = ParameterErrorCode.values().find { it.getCodeValue() == str } ?: ServerErrorCode.INTERNAL_SERVER_ERROR
    }
}

/**
 * i18n localeMessage Code based on exception for internal server errors
 *
 * @author Aivyss
 * @since 2022/12/26
 */
enum class ServerErrorCode : CodeEnum {
    INTERNAL_SERVER_ERROR,
    CODE_ERROR,
    ;

    override fun getCodeValue(): String = "EXCEPTION.SERVER.$name"

    companion object {
        fun matches(str: String?): CodeEnum = ServerErrorCode.values().find { it.getCodeValue() == str } ?: INTERNAL_SERVER_ERROR
    }
}