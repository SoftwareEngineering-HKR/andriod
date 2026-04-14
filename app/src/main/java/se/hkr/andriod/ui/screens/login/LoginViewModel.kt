package se.hkr.andriod.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.R
import se.hkr.andriod.data.network.AuthService
import se.hkr.andriod.data.network.ConnectionManager

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val loginError: String? = null,
    val navigateToHome: Boolean = false
)

class LoginViewModel(
    private val connectionManager: ConnectionManager = ConnectionManager(),
    private val authService: AuthService = AuthService()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChanged(username: String) {
        _uiState.update { it.copy(username = username, usernameError = null, loginError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null, loginError = null) }
    }

    fun onLoginClicked() {
        val currentState = _uiState.value
        var hasError = false
        var usernameErrorId: Int? = null
        var passwordErrorId: Int? = null

        if (currentState.username.isBlank()) {
            usernameErrorId = R.string.error_username_empty
            hasError = true
        }

        if (currentState.password.length < 4) {
            passwordErrorId = R.string.error_password_short
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    usernameError = usernameErrorId,
                    passwordError = passwordErrorId
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            try {
                val username = currentState.username
                val password = currentState.password

                // Discover backend first
                connectionManager.startConnection { ip ->

                    if (ip == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loginError = "Backend not found"
                            )
                        }
                        return@startConnection
                    }

                    // Login request
                    authService.login(ip, username, password) { success, result ->

                        if (!success) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    loginError = result ?: "Login failed"
                                )
                            }
                            return@login
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
                        loginError = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onNavigationDone() {
        _uiState.update { it.copy(navigateToHome = false) }
    }
}
