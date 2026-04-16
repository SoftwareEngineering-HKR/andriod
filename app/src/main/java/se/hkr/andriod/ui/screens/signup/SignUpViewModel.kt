package se.hkr.andriod.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.R

data class SignupUiState(
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val navigateToHome: Boolean = false
)

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    fun onUserNameChanged(firstName: String) {
        _uiState.update { it.copy(userName = firstName, userNameError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun onRegisterClicked() {
        val currentState = _uiState.value

        var hasError = false
        var firstNameErrorId: Int? = null
        var passwordErrorId: Int? = null
        var confirmPasswordErrorId: Int? = null

        if (currentState.userName.isBlank()) {
            firstNameErrorId = R.string.error_username_empty
            hasError = true
        }

        if (currentState.password.length < 8) {
            passwordErrorId = R.string.error_password_short
            hasError = true
        }

        if (currentState.password != currentState.confirmPassword) {
            confirmPasswordErrorId = R.string.error_passwords_do_not_match
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    userNameError = firstNameErrorId,
                    passwordError = passwordErrorId,
                    confirmPasswordError = confirmPasswordErrorId
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    userNameError = null,
                    passwordError = null,
                    confirmPasswordError = null
                )
            }

            delay(1500)

            _uiState.update {
                it.copy(isLoading = false, navigateToHome = true)
            }
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigateToHome = false) }
    }
}
