package com.codepied.api.file.domain

import com.codepied.api.api.config.ServerProfile
import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import javax.persistence.*

@Entity
@Table(name = "CODEPIED_FILE")
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
    val isPublic: Boolean,
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
            isPublic = isPublic,
        )
    }
}

interface CodepiedFileRepository : JpaRepository<CodepiedFile, Long> {
    fun findByFileIdAndPublic(fileId: String, isPublic: Boolean): CodepiedFile?
    fun findByIdAndPublic(id: Long, isPublic: Boolean): CodepiedFile?
    fun getByFileIdAndPublicIs(fileId: String, isPublic: Boolean): CodepiedFile {
        return this.findByFileIdAndPublic(fileId, isPublic) ?: throwInvalidRequest(
            errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
            debugMessage = "no file resource",
        )
    }
    fun getByIdAndPublic(id: Long, isPublic: Boolean): CodepiedFile {
        return this.findByIdAndPublic(id, isPublic) ?: throwInvalidRequest(
            errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
            debugMessage = "no file resource",
        )
    }

}