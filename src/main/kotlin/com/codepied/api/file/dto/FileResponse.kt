package com.codepied.api.file.dto

import java.io.InputStream

/**
 * file response format
 *
 * @author Aivyss
 * @since 2023/01/02
 */
data class FileResponse(
    val inputStream: InputStream,
    val mediaType: String,
    val fileName: String,
)