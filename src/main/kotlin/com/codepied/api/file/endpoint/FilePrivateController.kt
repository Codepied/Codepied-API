package com.codepied.api.file.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.file.application.FileService
import com.codepied.api.file.dto.FileCreate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/files/private")
class FilePrivateController(
    private val service: FileService,
) {
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createPublicFile(
        @Valid request: FileCreate
    ) = SuccessResponse(data = service.createPublicFile(request))
}