package com.codepied.api.test

import com.codepied.api.api.role.RoleType
import com.codepied.api.domain.User
import com.codepied.api.domain.UserFactory
import com.codepied.api.user.domain.ActivateStatus
import com.codepied.api.user.domain.UserCredentialFactory
import com.codepied.api.user.domain.UserDetailsFactory

object MockStore {
    fun createOneUser() = UserFactory.createUser("test@test.com", listOf(RoleType.USER), ActivateStatus.ACTIVATED)
    fun createOneUserCredential(user: User? = null) = UserCredentialFactory.create("password", user ?: createOneUser())
    fun createOneUserDetails(user: User? = null) = UserDetailsFactory.create("nickname", user ?: createOneUser())
}