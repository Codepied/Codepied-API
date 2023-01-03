package com.codepied.api.file.application

import com.codepied.api.api.config.RunnableEnvProperty
import com.codepied.api.api.exception.BusinessErrorCode
import com.codepied.api.api.exception.CodepiedBaseException.InvalidRequestException
import com.codepied.api.file.domain.CodepiedFile
import com.codepied.api.file.domain.CodepiedFileRepository
import com.codepied.api.file.dto.FileCreate
import com.codepied.api.test.AbstractServiceTest
import org.apache.tika.Tika
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.nio.file.Path

internal class FileServiceTest : AbstractServiceTest() {
    @Mock
    private lateinit var tika: Tika
    @Mock
    private lateinit var env: RunnableEnvProperty
    @Mock
    private lateinit var uploader: FileUploader
    @Mock
    private lateinit var downloader: FileDownloader
    @Mock
    private lateinit var fileRepository: CodepiedFileRepository

    @InjectMocks
    private lateinit var service: FileService

    @Test
    fun `파일 생성 테스트 성공`() {
        // * given
        val multipartFile = Mockito.mock(MultipartFile::class.java)
        doReturn(1000L).`when`(multipartFile).size
        doNothing().`when`(multipartFile).transferTo(any<Path>())
        doReturn("test").`when`(env).serverProfile
        doReturn("image/png").`when`(tika).detect(any<File>())
        doReturn("file_name").`when`(multipartFile).originalFilename
        doNothing().`when`(uploader).upload(any(), any())
        val fileMock = Mockito.mock(CodepiedFile::class.java)
        doReturn(fileMock).`when`(fileRepository).save(any())
        val fileCreate = FileCreate(multipartFile)


        // * when
        service.createPublicFile(fileCreate)
    }

    @Test
    fun `파일 생성 실패 - 서버프로파일 이상`() {
        // * given
        val multipartFile = Mockito.mock(MultipartFile::class.java)
        doReturn(1000L).`when`(multipartFile).size
        doNothing().`when`(multipartFile).transferTo(any<Path>())
        doReturn("invalid").`when`(env).serverProfile
        doReturn("file_name").`when`(multipartFile).originalFilename
        val fileCreate = FileCreate(multipartFile)

        // * when
        val throwable = catchThrowable { service.createPublicFile(fileCreate) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.FAIL_TO_FILE_UPLOAD)
    }

    @Test
    fun `파일 생성 실패 - 제한 용량 초과`() {
        // * when
        val multipartFile = Mockito.mock(MultipartFile::class.java)
        doReturn(3_000_001L).`when`(multipartFile).size
        doReturn("file_name").`when`(multipartFile).originalFilename
        val fileCreate = FileCreate(multipartFile)

        // * when
        val throwable = catchThrowable { service.createPublicFile(fileCreate) }

        // * then
        assertThat(throwable is InvalidRequestException).isTrue
        val exception = throwable as InvalidRequestException
        assertThat(exception.errorCode).isEqualTo(BusinessErrorCode.EXCEED_FILE_SIZE)
    }
    
    @Test
    fun `파일아이디 파일조회 성공`() {
        // * given
        val fileEntity = Mockito.mock(CodepiedFile::class.java)
        doReturn(fileEntity).`when`(fileRepository).getByFileIdAndPublicFile(anyString(), eq(true))
        val inputStream = Mockito.mock(InputStream::class.java)
        doReturn(inputStream).`when`(downloader).download(anyString())
        val fileName = "original.png"
        doReturn(fileName).`when`(fileEntity).originalName
        doReturn("image/png").`when`(fileEntity).mediaType

        // * when
        val fileResponse = service.retrievePublicFileById("testId")

        // * then
        assertThat(fileResponse.fileName).isEqualTo(fileName)
    }

    @Test
    fun `파일 숫자키에 의한 파일 조회 성공`() {
        // * given
        val fileEntity = Mockito.mock(CodepiedFile::class.java)
        doReturn(fileEntity).`when`(fileRepository).getByIdAndPublicFile(anyLong(), eq(true))
        val inputStream = Mockito.mock(InputStream::class.java)
        doReturn(inputStream).`when`(downloader).download(anyString())
        val fileName = "original.png"
        doReturn(fileName).`when`(fileEntity).originalName
        doReturn("image/png").`when`(fileEntity).mediaType
        doReturn("fileId").`when`(fileEntity).fileId

        // * when
        val fileResponse = service.retrievePublicFileByFileKey(1L)

        // * then
        assertThat(fileResponse.fileName).isEqualTo(fileName)
    }
}