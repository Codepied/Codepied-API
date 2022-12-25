package com.codepied.api.user.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.user.application.UserInfoService
import com.codepied.api.user.dto.ChangeEmailUserPassword
import com.codepied.api.user.dto.ChangeNickname
import com.codepied.api.user.dto.UserDataDuplicateType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * User info controller
 *
 * @author Aivyss
 * @since 2022/12/25
 */
@RestController
@RequestMapping("/api/users/info")
class UserInfoController(
    private val service: UserInfoService,
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/duplicate")
    fun checkDuplicatedEmail(
        @RequestParam(required = true) data: String,
        @RequestParam(required = true) type: String,
    ) :SuccessResponse<Boolean> {
        val duplicateType = UserDataDuplicateType.matches(type)

        return SuccessResponse(data = service.checkDuplicatedUserInfo(data, duplicateType))
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(params = ["type=PASSWORD"])
    fun changeEmailUserPassword(
        @Valid @RequestBody request: ChangeEmailUserPassword,
    ): SuccessResponse<Boolean> {
        service.changePassword(request.newPassword, request.oldPassword)

        return SuccessResponse(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(params=["type=NICKNAME"])
    fun changeNickname(
        @Valid @RequestBody request: ChangeNickname,
    ): SuccessResponse<Boolean> {
        service.changeNickname(request.nickname)

        return SuccessResponse(true)
    }
}