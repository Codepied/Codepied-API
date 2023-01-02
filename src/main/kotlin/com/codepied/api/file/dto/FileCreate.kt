package com.codepied.api.file.dto

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotNull

class FileCreate(
    @field:NotNull(message = "EXCEPTION.PARAMETERS.NOT_NULL")
    val file: MultipartFile,
) {
    val fileName = this.file.originalFilename ?: this.file?.name
}