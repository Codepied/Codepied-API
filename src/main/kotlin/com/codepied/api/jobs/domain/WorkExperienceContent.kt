package com.codepied.api.jobs.domain

import com.codepied.api.api.domain.Audit
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * work experience content
 *
 * @author Aivyss
 * @since 2023/01/08
 */
@Entity
@Table(name = "WORK_EXP_CONTENT")
@EntityListeners(AuditingEntityListener::class)
class WorkExperienceContent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXP_CONTENT_KEY")
    val id: Long,

    @Column(name = "SEQUENCE", nullable = false, updatable = true)
    var sequence: Int,

    @Column(name = "CONTENT", nullable = false, updatable = true)
    var content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_EXP_KEY")
    val workExperience: WorkExperience,
) {
    @Embedded
    val audit: Audit = Audit()
}