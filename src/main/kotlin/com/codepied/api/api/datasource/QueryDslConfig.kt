package com.codepied.api.api.datasource

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

/**
 * JPAQueryFactory Bean Configuration
 *
 * @author Aivyss
 * @since 2022/12/17
 */
@Configuration
class QueryDslConfig(
    val em: EntityManager,
) {
    @Bean
    fun queryFactory(): JPAQueryFactory = JPAQueryFactory(em)
}