package se.hkr.andriod.ui.screens.settings.subscreens.language

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.language.AppLanguage

data class LanguageUiState(
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val appliedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isSaving: Boolean = false
) {
    val hasChanges: Boolean
        get() = selectedLanguage != appliedLanguage
}

class LanguageViewModel : ViewModel() {

    val availableLanguages = AppLanguage.supportedLanguages
    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState

    fun onLanguageSelected(language: AppLanguage) {
        _uiState.update {
            it.copy(selectedLanguage = language)
        }
    }

    fun onSaveClicked() {
        val state = _uiState.value
        if (!state.hasChanges || state.isSaving) {
            return
        }

        _uiState.update {
            it.copy(isSaving = true)
        }

        _uiState.update {
            it.copy(appliedLanguage = it.selectedLanguage, isSaving = false,)
        }
    }
}
