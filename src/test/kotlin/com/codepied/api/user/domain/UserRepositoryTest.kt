package com.codepied.api.user.domain

import com.codepied.api.api.role.RoleType
import com.codepied.api.api.security.SocialType
import com.codepied.api.api.security.dto.PrincipalDetails
import com.codepied.api.test.MockStore.createOneUser
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Disabled
class UserRepositoryTest {
    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var em: EntityManager

    @Test
    @Transactional
    fun `audit 정상적용 테스트`() {
        val user = createOneUser()
        repository.save(user)
        Assertions.assertThat(user.audit.createdBy).isNull()
        em.clear()

        val principalDetails = PrincipalDetails(user.id, roleTypes = listOf(RoleType.USER), socialType = SocialType.EMAIL)
        SecurityContextHolder.getContext().authentication = principalDetails.createAuthentication()

        val findUser = repository.findByIdOrNull(user.id) ?: throw Exception()
        findUser.roles.add(UserRoleFactory.create(RoleType.PLATFORM_ADMIN, findUser))

        em.flush()
        em.clear()

        val findUser2 = repository.findByIdOrNull(user.id) ?: throw Exception()
        Assertions.assertThat(findUser2.roles.find { it.roleType == RoleType.PLATFORM_ADMIN }?.audit?.createdBy).isNotNull
    }
}