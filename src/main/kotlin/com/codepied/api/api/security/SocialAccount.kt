package com.codepied.api.api.security

interface SocialAccount {
    fun socialIdentification(): String
    fun email(): String
}