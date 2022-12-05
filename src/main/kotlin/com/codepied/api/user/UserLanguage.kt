package com.codepied.api.domain

import com.codepied.api.api.exception.ErrorCode
import com.codepied.api.api.exception.InvalidRequestExceptionBuilder.throwInvalidRequest
import com.codepied.api.api.http.SupportLanguage
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "USER_LANG")
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
    var supportLanguage: SupportLanguage = SupportLanguage.EN,
)

object UserLanguageFactory {
    fun create(
        user: User,
        supportLanguage: SupportLanguage,
    ): UserLanguage {
        if (user.id == 0L) {
            throwInvalidRequest(
                errorCode = ErrorCode.INTERNAL_SERVER_ERROR,
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
        errorCode = ErrorCode.NO_RESOURCE_ERROR,
        debugMessage = "no user language (actually zero case)"
    ) }
}