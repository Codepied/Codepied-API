package com.codepied.api.user.domain

import com.codepied.api.domain.UserRepository
import com.codepied.api.test.MockStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UserCredentialRepositoryTest {
    @Autowired
    private lateinit var userCredentialRepository: UserCredentialRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `user credential repository의 findByUserId 쿼리 테스트`() {
        val user = MockStore.createOneUser()
        userRepository.save(user)

        val credential = MockStore.createOneUserCredential(user)
        userCredentialRepository.save(credential)

        val findCredential = userCredentialRepository.findByUserId(user.id)

        assertThat(findCredential).isEqualTo(credential)
    }
}