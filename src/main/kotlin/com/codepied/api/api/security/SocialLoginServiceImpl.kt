package com.codepied.api.api.security

import com.codepied.api.domain.User
import org.springframework.stereotype.Service

@Service
class SocialLoginServiceImpl: SocialLoginService {
    override fun signup(socialType: SocialType, authorizationCode: String): LoginInfo {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }

    override fun login(socialType: SocialType, authorizationCode: String): LoginInfo {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }

    override fun logout(socialType: SocialType, authorizationCode: String, user: User) {
        when(socialType) {
            SocialType.GOOGLE -> {

            }
            SocialType.KAKAO -> {

            }
            SocialType.NAVER -> {

            }
            else -> null
        }
        TODO("Not yet implemented")
    }
}