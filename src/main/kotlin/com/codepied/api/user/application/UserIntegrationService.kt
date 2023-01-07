package com.codepied.api.user.application

import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwUnknownError
import com.codepied.api.api.externalApi.SocialLoginApiService
import com.codepied.api.api.http.RequestContext
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.application.EmailLoginService
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.dto.EmailUserLogin
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
    private val emailLoginService: EmailLoginService,
    private val socialUserIdentificationRepository: SocialUserIdentificationRepository,
    private val nativeQueryRepository: UserIntegrationNativeQueryRepository,
) {
    /**
     * social account -> email account
     * social account -> social account
     */
    @Transactional
    fun integrationSocialToAny(socialType: SocialType, authorizationCode: String) {
        val userKey = requestContext.userKey
        val socialAccount = socialLoginApiService.loginAuthorization(socialType, authorizationCode)
        val socialUserKey = socialUserIdentificationRepository
            .getBySocialTypeAndSocialIdentification(
                socialType = socialType,
                socialIdentification = socialAccount.socialIdentification()
            ).user.id

        integrationQuery(socialUserKey, userKey)
    }

    /**
     * email account -> social account
     */
    @Transactional
    fun integrationEmailToSocial(request: EmailUserLogin) {
        if (requestContext.socialType == SocialType.EMAIL) {
            throwUnknownError()
        }
        val userKey = requestContext.userKey
        val socialUserKey = emailLoginService.login(request).getUserKey()

        integrationQuery(socialUserKey, userKey)
    }

    private fun integrationQuery(socialUserKey: Long, userKey: Long) {
        when (socialUserKey == userKey) {
            true -> return
            false -> {
                try {
                    nativeQueryRepository.integration(socialUserKey, userKey)
                } catch (e: Exception) {
                    throwInvalidRequest(
                        errorCode = BusinessErrorCode.INTEGRATION_SQL_FAIL,
                        debugMessage = "user integrationEmailToSocial sql fail"
                    )
                }
            }
        }
    }

    fun retrieveIntegrationInfo(): List<Pair<SocialType, String?>> {
        return socialUserIdentificationRepository
            .findAllByUserId(requestContext.userKey)
            .filter { !it.audit.deleted }
            .associateBy { it.socialType }
            .entries.map { it.key to it.value.email }
    }
}