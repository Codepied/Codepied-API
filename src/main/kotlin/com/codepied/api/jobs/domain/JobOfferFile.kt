package com.codepied.api.jobs.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.file.domain.CodepiedFile
import com.codepied.api.user.domain.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

/**
 * File Entity for Job Offer platform
 *
 * @author Aivyss
 * @since 2023/01/08
 */
@Entity
@Table(name = "JOB_OFFER_FILE")
@EntityListeners(AuditingEntityListener::class)
class JobOfferFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_KEY")
    val id: Long,

    @Column(name = "TITLE", nullable = false, updatable = true, length = 128)
    var title: String,

    @OneToOne(fetch = FetchType.LAZY)
    @Column(name = "CODEPIED_FILE_KEY", nullable = false, updatable = false, unique = true)
    val file: CodepiedFile,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    val user: User,
) {
    @Embedded
    val audit: Audit = Audit()
}

object JobOfferFileFactory {
    fun create(title: String, file: CodepiedFile, user: User, id: Long = 0L): JobOfferFile {
        return JobOfferFile(
            id = id,
            title = title,
            file = file,
            user = user,
        )
    }
}

@Repository
interface JobOfferFileRepository : JpaRepository<JobOfferFile, Long>