package com.codepied.api.endpoint.dto

import com.codepied.api.api.http.SupportLanguage
import java.util.Locale

data class UserLanguageInfo(
    val languageId: SupportLanguage,
    val locale: Locale,
)