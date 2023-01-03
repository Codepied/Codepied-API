package com.codepied.api.api.file

import org.apache.tika.Tika
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileConfig {
    @Bean
    fun tika(): Tika = Tika()
}