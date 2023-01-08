package com.codepied.api.file.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.file.application.FileService
import com.codepied.api.file.dto.FileCreate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Controller
@RequestMapping("/api/files")
class FilePrivateController(
    private val service: FileService,
) {
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createPublicFile(
        @Valid request: FileCreate
    ) = SuccessResponse(data = service.createPrivateFile(request))

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = ["type=FILE_ID"])
    fun retrieveByFileId(
        @RequestParam fileId: String,
        model: ModelMap,
    ) : String {
        model["response"] = service.retrievePrivateFileById(fileId)

        return "file-download-view"
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = ["type=FILE_KEY"])
    fun retrieveByFileKey(
        @RequestParam fileKey: Long,
        model: ModelMap,
    ): String {
        model["response"] = service.retrievePrivateFileByFileKey(fileKey)

        return "file-download-view"
    }
}