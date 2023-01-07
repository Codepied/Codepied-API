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
                USER_KEY = :toUserKey
            WHERE 1 = 1
                AND USER_KEY = :fromUserKey
        """.trimIndent()

    fun integration(fromUserKey: Long, toUserKey: Long) {
        changeUserRoles(fromUserKey, toUserKey)
        changeSocialAccount(fromUserKey, toUserKey)
        changeUserLoginLog(fromUserKey, toUserKey)
        changeFileCreator(fromUserKey, toUserKey)
    }

    private fun changeSocialAccount(fromUserKey: Long, toUserKey: Long) {
        val sql = "UPDATE SOCIAL_USER_IDENTI $predicate"

        executeUpdate(em.createNativeQuery(sql), fromUserKey, toUserKey)
    }

    private fun changeUserRoles(fromUserKey: Long, toUserKey: Long) {
        val sql = """
            UPDATE MST_USER_ROLE
            $predicate
                AND ROLE_TYPE <> 'USER'
        """.trimIndent()

        executeUpdate(em.createNativeQuery(sql), fromUserKey, toUserKey)
    }

    private fun changeUserLoginLog(fromUserKey: Long, toUserKey: Long) {
        val sql = "UPDATE MST_USER_LOGIN_LOG $predicate"

        executeUpdate(em.createNativeQuery(sql), fromUserKey, toUserKey)
    }

    private fun changeFileCreator(fromUserKey: Long, toUserKey: Long) {
        val sql = """
            UPDATE CODEPIED_FILE
            SET CREATED_BY = :toUserKey
            WHERE 1 = 1
                AND CREATED_BY = :fromUserKey
        """.trimIndent()

        executeUpdate(em.createNativeQuery(sql), fromUserKey, toUserKey)
    }

    private fun executeUpdate(nativeQuery: Query, fromUserKey: Long, toUserKey: Long) {
        nativeQuery
            .setParameter("fromUserKey", fromUserKey)
            .setParameter("toUserKey", toUserKey)
            .executeUpdate()
    }
}