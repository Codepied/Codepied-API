package com.codepied.api.jobs.domain

import com.codepied.api.api.domain.Audit
import com.codepied.api.user.domain.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import javax.persistence.*

/**
 * Cover Letter
 *
 * @author Aivyss
 * @since 2023/01/08
 */
@Entity
@Table(name = "COVER_LETTER")
@EntityListeners(AuditingEntityListener::class)
class CoverLetter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CV_KEY")
    val id: Long,

    @Column(name = "TITLE", updatable = true, nullable = false, length = 128)
    var title: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    val user: User,
) {
    @OneToMany(mappedBy = "coverLetter", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val contents: MutableList<CVBlockTextContent> = mutableListOf()

    @OneToMany(mappedBy = "coverLetter", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val experiences: MutableList<WorkExperience> = mutableListOf()

    val audit: Audit = Audit()

    fun addTextBlock(title: String, content: String) {
        val contentSize = contents.size

        contents += CVBlockTextContent(
            id = 0L,
            sequence = contentSize,
            title = title,
            content = content,
            coverLetter = this,
        )
    }

    fun addExperience(
        companyName: String,
        position: String,
        startDate: LocalDate,
        endDate: LocalDate?,
        employed: Boolean,
        contents: List<String>
    ) {
        experiences += WorkExperience(
            id = 0L,
            companyName = companyName,
            position = position,
            startDate = startDate,
            endDate = endDate,
            employed = employed,
            coverLetter = this,
        ).apply { contents.forEach { this.addContent(it) } }
    }
}

object CoverLetterFactory {
    fun create(title: String, user: User, id: Long = 0L): CoverLetter {
        return CoverLetter(
            id = id,
            title = title,
            user = user,
        )
    }
}

interface CoverLetterRepository : JpaRepository<CoverLetter, Long>