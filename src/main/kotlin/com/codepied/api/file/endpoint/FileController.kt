package com.codepied.api.file.endpoint

import com.codepied.api.api.http.SuccessResponse
import com.codepied.api.file.application.FileService
import com.codepied.api.file.dto.FileCreate
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * endpoints related to file create / retrieve
 *
 * @author Aivyss
 * @since 2023/01/02
 */
@Controller
@RequestMapping("/api/files/public")
class FileController(
    private val service: FileService
) {
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    fun create(
        @Valid request: FileCreate
    ): SuccessResponse<Boolean> {
        service.createPublicFile(request)

        return SuccessResponse(data = true)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/", params = ["type=FILE_ID"])
    fun retrieveByFileId(
        @RequestParam fileId: String,
        model: ModelMap,
    ) : String {
        model["response"] = service.retrievePublicFileById(fileId)

        return "file-download-view"
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/", params = ["type=FILE_KEY"])
    fun retrieveByFileKey(
        @RequestParam fileKey: Long,
        model: ModelMap,
    ): String {
        model["response"] = service.retrievePublicFileByFileKey(fileKey)

        return "file-download-view"
    }
}