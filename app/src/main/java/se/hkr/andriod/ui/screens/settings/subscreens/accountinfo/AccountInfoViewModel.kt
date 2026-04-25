package se.hkr.andriod.ui.screens.settings.subscreens.accountinfo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.network.AuthSession

data class AccountInfoUiState(
    val username: String = "",
    val usernameInput: String = "",
    val isSaveEnabled: Boolean = false
)

class AccountInfoViewModel : ViewModel() {

    private val sessionUsername = AuthSession.getUsername().orEmpty()

    private val _uiState = MutableStateFlow(
        AccountInfoUiState(
            username = sessionUsername,
            usernameInput = sessionUsername,
            isSaveEnabled = false
        )
    )

    val uiState: StateFlow<AccountInfoUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(value: String) {
        val trimmed = value.trim()

        _uiState.update { state ->
            state.copy(
                usernameInput = value,
                isSaveEnabled = trimmed.isNotEmpty() && trimmed != state.username
            )
        }
    }
    fun saveUsername() {
        // TODO: Save username to backend
    }
}