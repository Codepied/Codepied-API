package com.codepied.api.file.application

import com.codepied.api.api.config.RunnableEnvProperty
import com.codepied.api.api.config.ServerProfile
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.file.TempFile
import com.codepied.api.file.domain.CodepiedFileFactory
import com.codepied.api.file.domain.CodepiedFileRepository
import com.codepied.api.file.dto.FileCreate
import com.codepied.api.file.dto.FileResponse
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * file service
 * @author Aivyss
 * @since 2023/01/02
 */
@Service
class FileService(
    private val tika: Tika,
    private val env: RunnableEnvProperty,
    private val uploader: FileUploader,
    private val downloader: FileDownloader,
    private val fileRepository: CodepiedFileRepository
) {
    @Transactional
    fun createPublicFile(request: FileCreate): String {
        if (request.file.size > 3_000_000) {
            throwInvalidRequest(
                errorCode = BusinessErrorCode.EXCEED_FILE_SIZE,
                debugMessage = "exceed file size",
            )
        }

        return try {
            TempFile.from(multipartFile = request.file).use { file ->
                val serverProfile = ServerProfile.matches(env.serverProfile)
                val fileEntity = CodepiedFileFactory.create(
                    mediaType = tika.detect(file.path.toFile()),
                    originalName = request.fileName,
                    serverProfile = serverProfile,
                    isPublic = true,
                )
                uploader.upload(fileEntity.fileId, file.path.toFile())

                fileRepository.save(fileEntity)

                fileEntity.fileId
            }
        } catch (e: Exception) {
            throwInvalidRequest(
                errorCode = BusinessErrorCode.FAIL_TO_FILE_UPLOAD,
                debugMessage = "fail to file upload"
            )
        }
    }

    fun retrievePublicFileById(fileId: String): FileResponse {
        // * public 검증
        val fileEntity = fileRepository.getByFileIdAndPublicFile(fileId = fileId, isPublicFile = true)

        // * retrieve file input stream
        val inputStream = downloader.download(fileId)

        return with(fileEntity) {
            FileResponse(
                inputStream = inputStream,
                fileName = originalName,
                mediaType = mediaType,
            )
        }
    }

    fun retrievePublicFileByFileKey(fileKey: Long): FileResponse {
        val fileEntity = fileRepository.getByIdAndPublicFile(id = fileKey, isPublicFile = true)

        // * retrieve file input stream
        val inputStream = downloader.download(fileEntity.fileId)

        return with(fileEntity) {
            FileResponse(
                inputStream = inputStream,
                fileName = originalName,
                mediaType = mediaType,
            )
        }
    }
}