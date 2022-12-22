package com.codepied.api.test

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.snippet.Snippet

object RestDocStore {
    fun responseSnippet(vararg fields: FieldDescriptor): Snippet {
        val chunks = mutableListOf<FieldDescriptor>()
        fields.forEach { chunks += it }

        chunks += fieldWithPath("httpStatus").type("HttpStatus").description("http status")
        chunks += fieldWithPath("success").type("Boolean").description("success status")
        chunks += fieldWithPath("supportLanguage").type("String").description("support language")
        chunks += fieldWithPath("responseTime").type("List<Int>").description("response time")

        return responseFields(chunks)
    }
}