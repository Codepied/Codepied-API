package com.codepied.api.api.security.application

import com.codepied.api.api.security.dto.LoginInfo
import com.codepied.api.user.dto.EmailUserCreate
import com.codepied.api.user.dto.EmailUserLogin
import java.util.UUID

/**
 * Email user login service interface
 *
 * @author Aivyss
 * @since 2022/12/17
 */
interface EmailLoginService {
    fun login(request: EmailUserLogin): LoginInfo
    fun signup(request: EmailUserCreate): LoginInfo
    fun activateAccountByEmailAuthorization(uuid: UUID)
}