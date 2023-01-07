package com.codepied.api.jobs.domain

import com.codepied.api.api.domain.Audit
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import javax.persistence.*

/**
 * work experience
 *
 * @author Aivyss
 * @since 2023/01/08
 */
@Entity
@Table(name = "WORK_EXPIRENCE")
@EntityListeners(AuditingEntityListener::class)
class WorkExperience(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_EXP_KEY")
    val id: Long,

    @Column(name = "COMPANY_NAME", updatable = true, nullable = false, length = 128)
    var companyName: String,

    @Column(name = "POSITION", updatable = true, nullable = false, length = 128)
    var position: String = "",

    @Column(name = "START_DATE", updatable = true, nullable = false)
    var startDate: LocalDate,

    @Column(name = "END_DATE", updatable = true, nullable = true)
    var endDate: LocalDate?,

    @Column(name = "EMPLOYED", nullable = false, updatable = true)
    var employed: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CV_KEY")
    val coverLetter: CoverLetter,
) {
    @OneToMany(mappedBy = "workExperience", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH])
    val contents: MutableList<WorkExperienceContent> = mutableListOf()

    @Embedded
    val audit: Audit = Audit()

    fun addContent(content: String) {
        contents += WorkExperienceContent(
            id = 0L,
            sequence = contents.size,
            workExperience = this,
        )
    }
}