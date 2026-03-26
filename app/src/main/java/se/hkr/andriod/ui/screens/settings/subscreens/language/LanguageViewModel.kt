package se.hkr.andriod.ui.screens.settings.subscreens.language

import se.hkr.andriod.data.language.AppLanguage

data class LanguageUiState(
    val availableLanguages: List<AppLanguage> = listOf(
        AppLanguage.ENGLISH,
        AppLanguage.HUNGARIAN
    ),
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val appliedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isSaving: Boolean = false
) {
    val hasChanges: Boolean
        get() = selectedLanguage != appliedLanguage
}
