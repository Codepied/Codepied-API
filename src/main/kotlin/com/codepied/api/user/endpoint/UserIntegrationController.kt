package com.codepied.api.user.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.application.UserIntegrationService
import com.codepied.api.user.dto.EmailUserLogin
import com.codepied.api.user.dto.SocialUserIntegrationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/users/integration")
class UserIntegrationController(
    private val service: UserIntegrationService,
) {
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    fun integration(
        @RequestBody @Valid request: SocialUserIntegrationRequest,
    ) = service.integrationSocialToAny(request.socialType, request.authorizationCode)
        .run{ SuccessResponse(data = true) }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    fun integration(
        @RequestBody @Valid request: EmailUserLogin,
    ) = service.integrationEmailToSocial(request)
        .run { SuccessResponse(data = true) }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun retrieveIntegrationInfo(): SuccessResponse<List<Pair<SocialType, String?>>> {
        return SuccessResponse(
            data = service.retrieveIntegrationInfo()
        )
    }
}