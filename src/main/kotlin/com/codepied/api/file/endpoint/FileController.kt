package com.codepied.api.file.endpoint

import com.codepied.api.file.application.FileService
import com.codepied.api.file.dto.FileCreate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * endpoints related to file create / retrieve
 * @author Aivyss
 * @since 2023/01/02
 */
@RestController
@RequestMapping("/api/files")
class FileController(
    private val service: FileService
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    fun create(
        @Valid request: FileCreate
    ) {
        service.create(request)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/", params = ["type=FILE_ID"])
    fun retrieveBy(
        @RequestParam fileId: String,
    ) {

    }
}