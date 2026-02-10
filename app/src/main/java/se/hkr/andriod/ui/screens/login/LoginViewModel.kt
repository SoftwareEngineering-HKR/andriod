package se.hkr.andriod.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.R

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val navigateToHome: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        var hasError = false
        var emailErrorId: Int? = null
        var passwordErrorId: Int? = null

        if (currentState.email.isBlank()) {
            emailErrorId = R.string.error_email_empty
            hasError = true
        }

        if (currentState.password.length < 8) {
            passwordErrorId = R.string.error_password_short
            hasError = true
        }

        if (hasError) {
            _uiState.update { it.copy(emailError = emailErrorId, passwordError = passwordErrorId) }
            return
        }

        // Simulate loading
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, emailError = null, passwordError = null) }
            delay(1500) // simulate network call
            _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigateToHome = false) }
    }
}
