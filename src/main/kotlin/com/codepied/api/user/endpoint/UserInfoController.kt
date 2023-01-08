package com.codepied.api.user.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.file.dto.FileIdRequest
import com.codepied.api.user.application.UserInfoService
import com.codepied.api.user.dto.ChangeEmailUserPassword
import com.codepied.api.user.dto.ChangeNickname
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
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

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(params=["type=PROFILE"])
    fun changeProfileImage(
        @RequestBody request: FileIdRequest,
    ) : SuccessResponse<Boolean> {
        service.changeProfileImage(request.fileId)

        return SuccessResponse(true)
    }
}