package com.codepied.api.user.persist

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.Query

@Repository
class UserIntegrationNativeQueryRepository(
    private val em: EntityManager,
) {
    private val predicate = """
            SET
                USER_KEY = :userKey
            WHERE 1 = 1
                AND USER_KEY = :socialUserKey
        """.trimIndent()

    fun integration(socialUserKey: Long, userKey: Long) {
        changeUserRoles(socialUserKey, userKey)
        changeSocialAccount(socialUserKey, userKey)
        changeUserLoginLog(socialUserKey, userKey)
        changeFileCreator(socialUserKey, userKey)
    }

    private fun changeSocialAccount(socialUserKey: Long, userKey: Long) {
        val sql = "UPDATE SOCIAL_USER_IDENTI $predicate"

        executeUpdate(em.createNativeQuery(sql), socialUserKey, userKey)
    }

    private fun changeUserRoles(socialUserKey: Long, userKey: Long) {
        val sql = """
            UPDATE MST_USER_ROLE
            $predicate
                AND ROLE_TYPE <> 'USER'
        """.trimIndent()

        executeUpdate(em.createNativeQuery(sql), socialUserKey, userKey)
    }

    private fun changeUserLoginLog(socialUserKey: Long, userKey: Long) {
        val sql = "UPDATE MST_USER_LOGIN_LOG $predicate"

        executeUpdate(em.createNativeQuery(sql), socialUserKey, userKey)
    }

    private fun changeFileCreator(socialUserKey: Long, userKey: Long) {
        val sql = """
            UPDATE CODEPIED_FILE
            SET CREATED_BY = :userKey
            WHERE 1 = 1
                AND CREATED_BY = :socialUserKey
        """.trimIndent()

        executeUpdate(em.createNativeQuery(sql), socialUserKey, userKey)
    }

    private fun executeUpdate(nativeQuery: Query, socialUserKey: Long, userKey: Long) {
        nativeQuery
            .setParameter("socialUserKey", socialUserKey)
            .setParameter("userKey", userKey)
            .executeUpdate()
    }
}