package com.codepied.api.file.application

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectResult
import com.codepied.api.api.config.AwsProperty
import com.codepied.api.api.file.TempFile
import com.codepied.api.test.AbstractServiceTest
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn

internal class FileUploaderTest : AbstractServiceTest() {
    @Mock
    private lateinit var s3Client: AmazonS3
    @Mock
    private lateinit var property: AwsProperty

    @InjectMocks
    private lateinit var service: FileUploader

    @Test
    fun `파일 업로드 성공`() {
        // * given
        doReturn("bucket-name").`when`(property).s3BucketName
        val resultMock = Mockito.mock(PutObjectResult::class.java)
        doReturn(resultMock).`when`(s3Client).putObject(any())

        // * when
        TempFile.empty().use {
            service.upload("id", it.path.toFile())
        }
    }
}