package com.codepied.api.user

import com.codepied.api.api.domain.Audit
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.http.SupportLanguage
import com.codepied.api.user.domain.User
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "USER_LANG")
@EntityListeners(AuditingEntityListener::class)
class UserLanguage(
    @Id
    @Column(name = "USER_KEY")
    val id: Long,

    /**
     * unfortunately hibernate doesn't support LAZY loading of OneToOne and MapsId relations
     */
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @MapsId("id")
    @JoinColumn(name = "USER_KEY", nullable = false, updatable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "SUPPORT_LANG", nullable = false, updatable = true)
    var supportLanguage: SupportLanguage = SupportLanguage.KO,

    @Embedded
    val audit: Audit = Audit(),
)

object UserLanguageFactory {
    fun create(
        user: User,
        supportLanguage: SupportLanguage,
    ): UserLanguage {
        if (user.id == 0L) {
            throwInvalidRequest(
                errorCode = BusinessErrorCode.UNKNOWN_ERROR,
                debugMessage = "signup fail due to support language (actually zero case)"
            )
        }

        return UserLanguage(id = user.id, user = user, supportLanguage = supportLanguage)
    }
}

@Repository
interface UserLanguageRepository : JpaRepository<UserLanguage, Long> {
    @EntityGraph(attributePaths = ["user"])
    override fun findById(id: Long): Optional<UserLanguage>
}

fun UserLanguageRepository.getLangById(id: Long): UserLanguage {
    return this.findById(id).orElseThrow { throwInvalidRequest(
        errorCode = BusinessErrorCode.NO_RESOURCE_ERROR,
        debugMessage = "no user language (actually zero case)"
    ) }
}