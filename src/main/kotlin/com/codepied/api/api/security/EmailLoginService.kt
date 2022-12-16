package com.codepied.api.api.security

import com.codepied.api.endpoint.dto.EmailUserLogin
import org.springframework.stereotype.Service

interface EmailLoginService {
    fun login(request: EmailUserLogin): LoginInfo
}

@Service
class EmailLoginServiceImpl: EmailLoginService {
    override fun login(request: EmailUserLogin): LoginInfo {
        TODO("Not yet implemented")
    }
}