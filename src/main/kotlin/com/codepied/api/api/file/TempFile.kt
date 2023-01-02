package com.codepied.api.api.file

import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * Auto closeable file. Should use this class when file process
 * @author Aivyss
 * @since 2023/01/02
 */
class TempFile(
    val path: Path
) : AutoCloseable {

    override fun close() {
        Files.delete(path)
    }

    companion object {
        fun from(multipartFile: MultipartFile): TempFile {
            val file = empty()
            multipartFile.transferTo(file.path)

            return file
        }

        fun empty(): TempFile {
            return TempFile(Files.createTempFile("", UUID.randomUUID().toString()))
        }
    }
}