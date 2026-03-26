package se.hkr.andriod.ui.screens.settings.subscreens.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.data.language.AppLanguage
import se.hkr.andriod.data.language.LanguageStorage

data class LanguageUiState(
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val appliedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isSaving: Boolean = false
) {
    val hasChanges: Boolean
        get() = selectedLanguage != appliedLanguage
}

class LanguageViewModel(
    private val languageStorage: LanguageStorage
) : ViewModel() {

    val availableLanguages = AppLanguage.supportedLanguages
    private val _uiState = MutableStateFlow(LanguageUiState())
    val uiState: StateFlow<LanguageUiState> = _uiState

    init {
        viewModelScope.launch {
            languageStorage.selectedLanguageFlow.collect { savedLanguage ->
                _uiState.update {
                    it.copy(
                        selectedLanguage = savedLanguage,
                        appliedLanguage = savedLanguage
                    )
                }
            }
        }
    }
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
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSaving = true)
            }

            languageStorage.saveLanguage(state.selectedLanguage)

            _uiState.update {
                it.copy(appliedLanguage = it.selectedLanguage, isSaving = false)
            }
        }
    }
}
