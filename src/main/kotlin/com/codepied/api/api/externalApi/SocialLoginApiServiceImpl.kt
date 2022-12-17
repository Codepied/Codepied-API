package com.codepied.api.api.externalApi

import com.codepied.api.api.security.SocialAccount
import com.codepied.api.api.security.SocialType
import org.springframework.stereotype.Service

@Service
class SocialLoginApiServiceImpl(
    kakaoApiService: KakaoLoginApiService,
    naverApiService: NaverLoginApiService,
    googleApiService: GoogleLoginApiService
): SocialLoginApiService {
    private val apiService: Map<SocialType, SpecificProviderLoginApiService> = mapOf(
        SocialType.KAKAO to kakaoApiService,
        SocialType.NAVER to naverApiService,
        SocialType.GOOGLE to googleApiService,
    )

    override fun loginAuthorization(socialType: SocialType, authorizationCode: String): SocialAccount {
        val socialAccount = apiService[socialType]?.login(authorizationCode)
        TODO("Not yet implemented")
    }
}