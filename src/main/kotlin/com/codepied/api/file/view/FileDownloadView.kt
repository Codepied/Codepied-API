package com.codepied.api.file.view

import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.AbstractView
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
        TODO("Not yet implemented")
    }
}