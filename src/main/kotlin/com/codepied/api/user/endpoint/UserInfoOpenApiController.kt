package com.codepied.api.user.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.user.application.UserInfoService
import com.codepied.api.user.dto.UserDataDuplicateType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/open-api/users/info")
class UserInfoOpenApiController(
    private val service: UserInfoService,
) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/duplicate")
    fun checkDuplicatedEmail(
        @RequestParam(required = true) data: String,
        @RequestParam(required = true) type: String,
    ) : SuccessResponse<Boolean> {
        val duplicateType = UserDataDuplicateType.matches(type)

        return SuccessResponse(data = service.checkDuplicatedUserInfo(data, duplicateType))
    }
}