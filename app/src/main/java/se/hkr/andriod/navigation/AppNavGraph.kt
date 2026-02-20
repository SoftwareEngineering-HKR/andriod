package se.hkr.andriod.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.hkr.andriod.ui.screens.deviceoverview.DeviceOverviewEvent
import se.hkr.andriod.ui.screens.deviceoverview.DeviceOverviewScreen
import se.hkr.andriod.ui.screens.login.LoginScreen
import se.hkr.andriod.ui.screens.login.LoginViewModel
import se.hkr.andriod.ui.screens.signup.SignUpScreen
import se.hkr.andriod.ui.screens.signup.SignUpViewModel
import se.hkr.andriod.ui.screens.devicecard.DeviceCardScreen
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel
import se.hkr.andriod.ui.screens.devicecard.DeviceScreenUiState

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DEVICE_CARD // CHANGED
    ) {
        /* ! Disabled until making the DeviceCardScreen
        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    // Navigate to device overview after login
                    navController.navigate(Routes.DEVICE_OVERVIEW) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignUpClicked = {
                    navController.navigate(Routes.SIGN_UP)
                }
            )
        }

        composable(Routes.SIGN_UP) {

            val registerViewModel: SignUpViewModel = viewModel()

            SignUpScreen(
                viewModel = registerViewModel,
                onNavigateToHome = {
                    // Navigate to device overview after signup
                    navController.navigate(Routes.DEVICE_OVERVIEW) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginClicked = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.DEVICE_OVERVIEW) {
            DeviceOverviewScreen { event ->
                when (event) {
                    DeviceOverviewEvent.LogOutClicked -> {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.DEVICE_OVERVIEW) { inclusive = true }
                        }
                    }
                }
            }
        }*/

        composable(Routes.DEVICE_CARD) {
            DeviceCardScreen(
                viewModel = DeviceCardViewModel()
            )
        }
    }
}