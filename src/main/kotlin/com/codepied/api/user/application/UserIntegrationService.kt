package com.codepied.api.user.application

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.externalApi.SocialLoginApiService
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.persist.UserIntegrationNativeQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Social - Email Account Integration Service
 * @author Aivyss
 * @since 2023/01/06
 */
@Service
class UserIntegrationService(
    private val requestContext: RequestContext,
    private val socialLoginApiService: SocialLoginApiService,
    private val socialUserIdentificationRepository: SocialUserIdentificationRepository,
    private val nativeQueryRepository: UserIntegrationNativeQueryRepository,
) {
    @Transactional
    fun integration(socialType: SocialType, authorizationCode: String) {
        val userKey = requestContext.userKey
        val socialAccount = socialLoginApiService.loginAuthorization(socialType, authorizationCode)
        val socialUserKey = socialUserIdentificationRepository
            .getBySocialTypeAndSocialIdentification(
                socialType = socialType,
                socialIdentification = socialAccount.socialIdentification()
            ).user.id

        when (socialUserKey == userKey) {
            true -> return
            false -> {
                try {
                    nativeQueryRepository.integration(socialUserKey, userKey)
                } catch(e: Exception) {
                    throwInvalidRequest(
                        errorCode = BusinessErrorCode.INTEGRATION_SQL_FAIL,
                        debugMessage = "user integration sql fail"
                    )
                }
            }
        }
    }
}