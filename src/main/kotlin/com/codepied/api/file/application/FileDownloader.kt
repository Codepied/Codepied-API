package com.codepied.api.file.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GetObjectRequest
import com.codepied.api.api.config.AwsProperty
import org.springframework.stereotype.Service
import java.io.InputStream

/**
 * file downloader
 *
 * @author Aivyss
 * @since 2023/01/02
 */
@Service
class FileDownloader(
    private val s3Client: AmazonS3,
    private val property: AwsProperty,
) {
    fun download(fileId: String): InputStream {
        return s3Client
            .getObject(GetObjectRequest(property.s3BucketName, fileId))
            .objectContent
            .buffered(bufferSize = 2048)
    }
}