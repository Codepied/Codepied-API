package com.codepied.api.user.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.api.security.SocialType
import com.codepied.api.user.application.UserIntegrationService
import com.codepied.api.user.dto.UserIntegrationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
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
        @RequestBody @Valid request: UserIntegrationRequest,
    ) : SuccessResponse<Boolean> {
        service.integration(request.socialType, request.authorizationCode)

        return SuccessResponse(data = true)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    fun retrieveIntegrationInfo(): SuccessResponse<List<Pair<SocialType, String?>>> {
        return SuccessResponse(
            data = service.retrieveIntegrationInfo()
        )
    }
}