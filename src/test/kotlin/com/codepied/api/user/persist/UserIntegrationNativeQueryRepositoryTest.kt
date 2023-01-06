package com.codepied.api.user.persist

import com.codepied.api.test.AbstractServiceTest
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import javax.persistence.EntityManager
import javax.persistence.Query

internal class UserIntegrationNativeQueryRepositoryTest : AbstractServiceTest() {
    @Mock
    private lateinit var em: EntityManager

    @InjectMocks
    private lateinit var repository: UserIntegrationNativeQueryRepository

    @Test
    fun `유저 통합 성공`() {
        // * given
        val nativeQuery = Mockito.mock(Query::class.java)
        doReturn(nativeQuery).`when`(em).createNativeQuery(anyString())
        doReturn(nativeQuery).`when`(nativeQuery).setParameter(anyString(), anyLong())
        doReturn(1).`when`(nativeQuery).executeUpdate()

        // * when
        repository.integration(1L, 1L)
    }
}