package se.hkr.andriod.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.data.network.AuthService
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.ui.screens.login.LoginViewModel
import se.hkr.andriod.ui.screens.signup.SignUpViewModel

class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val authService = AuthService(context.applicationContext)
        val connectionManager = ConnectionManager()

        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(connectionManager, authService) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(connectionManager, authService) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
