package com.codepied.api.api

/**
 * enum interface for presentation interface messages
 *
 * @author Aivyss
 * @since 11/30/2022
 */
interface CodeEnum {
    val name: String
    fun getCodeValue(): String
    fun getNameValue(): String = this.name
}