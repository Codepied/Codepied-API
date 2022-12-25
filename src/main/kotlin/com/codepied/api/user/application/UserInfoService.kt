package com.codepied.api.user.application

import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidPassword
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwNoSuchUser
import com.codepied.api.api.http.RequestContext
import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.domain.UserCredentialRepository
import com.codepied.api.user.domain.UserDetailsRepository
import com.codepied.api.user.dto.UserDataDuplicateType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

/**
 * User info service
 *
 * @author Aivyss
 * @since 2022/12/25
 */
@Service
@Transactional
class UserInfoService(
    private val socialUserIdentificationRepository: SocialUserIdentificationRepository,
    private val userDetailsRepository: UserDetailsRepository,
    private val userCredentialRepository: UserCredentialRepository,
    private val requestContext: RequestContext,
    private val passwordEncoder: PasswordEncoder,
) {
    private val duplicateObserver: Map<UserDataDuplicateType, KFunction<*>?>

    init {
        fun matchesMethod(methodName: String): KFunction<*>? {
            return this::class.declaredFunctions.find {
                it.name == methodName
            }
        }

        this::class.declaredFunctions.forEach { it.isAccessible = true }

        this.duplicateObserver = mapOf(
            UserDataDuplicateType.EMAIL to matchesMethod("checkDuplicatedEmail"),
            UserDataDuplicateType.NICKNAME to matchesMethod("checkDuplicatedNickname")
        )
    }

    fun checkDuplicatedUserInfo(data: String, type: UserDataDuplicateType) = duplicateObserver[type]
        ?.call(this, data) == true

    fun changePassword(newPassword: String, oldPassword: String) {
        val userCredential = userCredentialRepository.findByUserId(requestContext.userKey)
            ?: throwNoSuchUser()

        if (!passwordEncoder.matches(oldPassword, userCredential.password)) {
            throwInvalidPassword()
        }

        userCredential.password = passwordEncoder.encode(newPassword)
    }

    fun  changeNickname(nickname: String) {
        userDetailsRepository.findByUserId(requestContext.userKey)?.apply {
            this.nickname = nickname
        } ?: throwNoSuchUser()
    }

    private fun checkDuplicatedEmail(email: String) = socialUserIdentificationRepository.existsByEmail(email)
    private fun checkDuplicatedNickname(nickname: String) = userDetailsRepository.existsByNickname(nickname)
}