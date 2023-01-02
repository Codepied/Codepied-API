package com.codepied.api.file.domain

import com.codepied.api.api.config.ServerProfile
import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "CODEPIED_FILE")
@EntityListeners(AuditingEntityListener::class)
class CodepiedFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_KEY")
    val id: Long,

    @Column(name = "ORIGINAL_NAME", nullable = false, updatable = false)
    val originalName: String,

    @Column(name = "FILE_ID", nullable = false, updatable = false, unique = true)
    val fileId: String,

    @Column(name = "MEDIA_TYPE", nullable = false, updatable = false)
    val mediaType: String,

    @Column(name = "IS_PUBLIC_FILE", nullable =false, updatable = false)
    val publicFile: Boolean,
) {
    @Embedded
    val audit: Audit = Audit()
}

object CodepiedFileFactory {
    fun create(mediaType: String, originalName: String, serverProfile: ServerProfile, isPublic: Boolean, id: Long = 0L): CodepiedFile {
        return CodepiedFile(
            id = id,
            originalName = originalName,
            fileId = "${UUID.randomUUID()}/codepied-core:server:${serverProfile.name}",
            mediaType = mediaType,
            publicFile = isPublic,
        )
    }
}

interface CodepiedFileRepository : JpaRepository<CodepiedFile, Long> {
    fun findByFileIdAndPublicFile(fileId: String, isPublicFile: Boolean): CodepiedFile?
    fun findByIdAndPublicFile(id: Long, isPublicFile: Boolean): CodepiedFile?
    fun getByFileIdAndPublicFile(fileId: String, isPublicFile: Boolean): CodepiedFile {
        return this.findByFileIdAndPublicFile(fileId, isPublicFile) ?: throwInvalidRequest(
            errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
            debugMessage = "no file resource",
        )
    }
    fun getByIdAndPublicFile(id: Long, isPublicFile: Boolean): CodepiedFile {
        return this.findByIdAndPublicFile(id, isPublicFile) ?: throwInvalidRequest(
            errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
            debugMessage = "no file resource",
        )
    }

}