package com.codepied.api.test

import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.domain.*

object MockStore {
    fun createOneUser() = UserFactory.createUser(listOf(RoleType.USER), ActivateStatus.ACTIVATED).apply {
        this.socialIdentifications += SocialUserIdentificationFactory.create(
            socialIdentification = this.id.toString(),
            socialType = SocialType.EMAIL,
            user = this,
            email = "test@test.com"
        )
    }
    fun createOneUserCredential(user: User? = null) = UserCredentialFactory.create("password", user ?: createOneUser())
    fun createOneUserDetails(user: User? = null) = UserDetailsFactory.create("nickname", user ?: createOneUser(), null)
}