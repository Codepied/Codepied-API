package com.codepied.api

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
@EnableAutoConfiguration // I just add this annotation due to intelliJ indexing bug. (Aivyss)
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
