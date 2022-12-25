package com.codepied.api.user.application

import com.codepied.api.user.domain.SocialUserIdentificationRepository
import com.codepied.api.user.dto.UserDataDuplicateType
import org.springframework.stereotype.Service
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
class UserInfoService(
    private val socialUserIdentificationRepository: SocialUserIdentificationRepository,
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
            UserDataDuplicateType.EMAIL to matchesMethod("checkDuplicatedEmail")
        )
    }

    fun checkDuplicatedUserInfo(data: String, type: UserDataDuplicateType) : Boolean {
        val checkFunction = duplicateObserver[type]
        return checkFunction?.call(this, data) == true
    }

    private fun checkDuplicatedEmail(email: String) = socialUserIdentificationRepository.existsByEmail(email)
}