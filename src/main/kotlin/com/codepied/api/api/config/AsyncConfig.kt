package com.codepied.api.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


/**
 * Asynchronous Thread Pool Configuration
 *
 * @author Aivyss
 * @since 2022/12/18
 */
@EnableAsync
@Configuration
class AsyncConfig(
    private val asyncProperty: AsyncProperty,
) {
    @Bean(name = ["asyncThreadTaskExecutor"])
    fun asyncThreadTaskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()

        with(asyncProperty) {
            executor.corePoolSize = asyncPoolMinCapacity
            executor.maxPoolSize = asyncPoolMaxCapacity
            executor.queueCapacity = asyncPoolQueueCapacity
        }

        executor.setThreadNamePrefix("Async-pool-")
        executor.initialize()

        return executor
    }
}