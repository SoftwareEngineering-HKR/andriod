package se.hkr.andriod.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.R
import se.hkr.andriod.data.network.AuthService
import se.hkr.andriod.data.network.ConnectionManager

data class SignupUiState(
    val userName: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val registerError: String? = null,
    val navigateToHome: Boolean = false
)

class SignUpViewModel(
    private val connectionManager: ConnectionManager = ConnectionManager(),
    private val authService: AuthService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    fun onUserNameChanged(userName: String) {
        _uiState.update { it.copy(userName = userName, userNameError = null, registerError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, registerError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null, registerError = null) }
    }

    fun onRegisterClicked() {
        val currentState = _uiState.value

        var hasError = false
        var userNameErrorId: Int? = null
        var passwordErrorId: Int? = null
        var confirmPasswordErrorId: Int? = null

        if (currentState.userName.isBlank()) {
            userNameErrorId = R.string.error_username_empty
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
                    userNameError = userNameErrorId,
                    passwordError = passwordErrorId,
                    confirmPasswordError = confirmPasswordErrorId
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            try {
                val username = currentState.userName
                val password = currentState.password

                // Discover backend first
                connectionManager.startConnection { ip ->

                    if (ip == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                registerError = "Backend not found"
                            )
                        }
                        return@startConnection
                    }

                    // Register request
                    authService.register(ip, username, password) { success, result ->

                        if (!success) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    registerError = result ?: "Register failed"
                                )
                            }
                            return@register
                        }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                navigateToHome = true
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registerError = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigateToHome = false) }
    }
}
