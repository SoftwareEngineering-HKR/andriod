package se.hkr.andriod.ui.screens.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ScanUiState(
    val isRetrying: Boolean = false
)

class ScanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScanUiState())
    val uiState: StateFlow<ScanUiState> = _uiState

    fun onRetryClick() {
        if (!_uiState.value.isRetrying) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isRetrying = true)
            }

            // Fake retry
            delay(1500)

            _uiState.update {
                it.copy(isRetrying = false)
            }
        }
    }

}
