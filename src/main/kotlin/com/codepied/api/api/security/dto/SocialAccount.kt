package com.codepied.api.api.security.dto

interface SocialAccount {
    fun socialIdentification(): String
    fun email(): String
}