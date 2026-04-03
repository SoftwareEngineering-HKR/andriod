package se.hkr.andriod.ui.screens.settings.subscreens.accountinfo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.mock.currentUser
import se.hkr.andriod.domain.model.user.User

data class AccountInfoUiState(
    val user: User? = null,
    val usernameInput: String = "",
    val isEditMode: Boolean = false,
    val isSaveEnabled: Boolean = false
)

class AccountInfoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        AccountInfoUiState(
            user = currentUser,
            usernameInput = currentUser.username,
            isEditMode = false,
            isSaveEnabled = false
        )
    )

    val uiState: StateFlow<AccountInfoUiState> = _uiState.asStateFlow()

    fun enterEditMode() {
        _uiState.update { state ->
            state.copy(
                isEditMode = true,
                usernameInput = state.user?.username.orEmpty(),
                isSaveEnabled = false
            )
        }
    }

    fun onUsernameChanged(value: String) {
        _uiState.update { state ->
            val trimmed = value.trim()
            val currentUsername = state.user?.username.orEmpty()

            state.copy(
                usernameInput = value,
                isSaveEnabled = trimmed != currentUsername && trimmed.isNotEmpty()
            )
        }
    }

    fun cancelEdit() {
        _uiState.update { state ->
            state.copy(
                usernameInput = state.user?.username.orEmpty(),
                isEditMode = false,
                isSaveEnabled = false
            )
        }
    }

    fun saveUsername() {
        val state = _uiState.value
        val user = state.user ?: return

        val trimmedUsername = state.usernameInput.trim()
        if (trimmedUsername == user.username) return
        if (trimmedUsername.isBlank()) return

        val updatedUser = user.copy(username = trimmedUsername)
        currentUser = updatedUser

        _uiState.update { state ->
            state.copy(
                user = updatedUser,
                usernameInput = updatedUser.username,
                isEditMode = false,
                isSaveEnabled = false
            )
        }
    }
}