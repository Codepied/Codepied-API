package com.codepied.api.file.endpoint

import com.codepied.api.file.application.FileService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * endpoints related to file create / retrieve
 *
 * @author Aivyss
 * @since 2023/01/02
 */
@Controller
@RequestMapping("/api/files/public")
class FilePublicController(
    private val service: FileService
) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = ["type=FILE_ID"])
    fun retrieveByFileId(
        @RequestParam fileId: String,
        model: ModelMap,
    ) : String {
        model["response"] = service.retrievePublicFileById(fileId)

        return "file-download-view"
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(params = ["type=FILE_KEY"])
    fun retrieveByFileKey(
        @RequestParam fileKey: Long,
        model: ModelMap,
    ): String {
        model["response"] = service.retrievePublicFileByFileKey(fileKey)

        return "file-download-view"
    }
}