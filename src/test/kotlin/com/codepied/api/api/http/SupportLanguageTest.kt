package com.codepied.api.api.http

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SupportLanguageTest {
    @Test
    fun `적합하지 않은 언어코드에서 한국어 디폴트 - null`() {
        // * when
        val supportLanguage = SupportLanguage.matches(null)

        // * then
        Assertions.assertThat(supportLanguage).isEqualTo(SupportLanguage.KO)
    }

    @Test
    fun `적합하지 않은 언어코드에서 한국어 디폴트 - not null`() {
        // * when
        val supportLanguage = SupportLanguage.matches("invalidLang")

        // * then
        Assertions.assertThat(supportLanguage).isEqualTo(SupportLanguage.KO)
    }

    @Test
    fun `적합한 언어코드`() {
        val supportLanguage = SupportLanguage.matches("en")
        Assertions.assertThat(supportLanguage).isEqualTo(SupportLanguage.EN)
    }
}