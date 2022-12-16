package com.codepied.api.api.security

interface SocialAccount {
    fun socialIdentification(): String
    fun getEmail(): String
}