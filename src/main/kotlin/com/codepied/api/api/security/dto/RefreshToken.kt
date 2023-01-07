package com.codepied.api.api.security.dto

import com.codepied.api.api.security.SocialType

class RefreshTokenBody(
    val userKey: Long,
    val socialType: SocialType,
)