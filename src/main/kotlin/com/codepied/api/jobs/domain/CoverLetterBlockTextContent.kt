package com.codepied.api.jobs.domain

import com.codepied.api.api.domain.Audit
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

/**
 * Cover Letter Block Text Content
 *
 * @author Aivyss
 * @since 2023/01/08
 */
@Entity
@Table(name = "CV_BLOCK_TEXT_CONTENT")
@EntityListeners(AuditingEntityListener::class)
class CVBlockTextContent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENT_KEY")
    val id: Long,

    @Column(name = "SEQUENCE", nullable = false, updatable = true)
    val sequence: Int,

    @Column(name = "TITLE", nullable = false, updatable = true, length = 128)
    var title: String = "",

    @Column(name = "CONTENT", nullable = false, updatable = true)
    val content: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CV_KEY")
    val coverLetter: CoverLetter,
) {
    @Embedded
    val Audit: Audit = Audit()
}