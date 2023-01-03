package com.codepied.api.file.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.codepied.api.api.config.AwsProperty
import org.springframework.stereotype.Service
import java.io.File

@Service
class FileUploader(
    private val s3Client: AmazonS3,
    private val property: AwsProperty,
) {
    fun upload(id: String, file: File) {
        val request = PutObjectRequest(property.s3BucketName, id, file)

        s3Client.putObject(request)
    }
}