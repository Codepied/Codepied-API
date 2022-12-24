package com.codepied.api.api.externalApi

import com.codepied.api.api.security.dto.SocialAccount
import com.codepied.api.api.security.SocialType

/**
 * Social Login Service Interface
 *
 * @author Aivyss
 * @since 2022/12/24
 */
interface SpecificProviderLoginApiService {
    fun login(authorizationCode: String): SocialAccount
    fun supportType(): SocialType
}