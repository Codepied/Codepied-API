package com.codepied.api.user.dto

import com.codepied.api.api.http.SupportLanguage
import java.util.Locale

data class UserLanguageInfo(
    val languageId: SupportLanguage,
    val locale: Locale,
)