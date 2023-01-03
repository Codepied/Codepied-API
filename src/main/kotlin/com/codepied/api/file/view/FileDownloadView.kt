package com.codepied.api.file.view

import com.codepied.api.file.dto.FileResponse
import org.apache.tika.metadata.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.FileCopyUtils
import org.springframework.web.servlet.view.AbstractView
import java.io.BufferedInputStream
import java.net.URLEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * file download view
 * @author Aivyss
 * @since 2023/01/02
 */
@Component("file-download-view")
class FileDownloadView : AbstractView() {
    override fun renderMergedOutputModel(
        model: MutableMap<String, Any>,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        model["response"].let {
            when(it) {
                is FileResponse -> {
                    setHeader(response, it)

                    val stream = if (it.inputStream is BufferedInputStream) {
                        it.inputStream
                    } else {
                        BufferedInputStream(it.inputStream)
                    }

                    stream.use { FileCopyUtils.copy(stream, response.outputStream) }
                }
                else -> null
            }
        }
    }

    private fun setHeader(response: HttpServletResponse, fileResponse: FileResponse) {
        response.contentType = fileResponse.mediaType
        val fileName = URLEncoder.encode(fileResponse.fileName, "UTF-8")
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            """${fileResponse.contentDisPosition}; filename="$fileName""""
        )
        response.addHeader("X-Content-Type-Options", "nosniff")
    }
}