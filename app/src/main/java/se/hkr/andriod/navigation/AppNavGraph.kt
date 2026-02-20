package se.hkr.andriod.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.hkr.andriod.ui.screens.devicecard.DeviceCardScreen
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel
import se.hkr.andriod.ui.screens.login.LoginScreen
import se.hkr.andriod.ui.screens.login.LoginViewModel
import se.hkr.andriod.ui.screens.main.MainScreen
import se.hkr.andriod.ui.screens.signup.SignUpScreen
import se.hkr.andriod.ui.screens.signup.SignUpViewModel

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DEVICE_CARD // CHANGED
    ) {
        /* Disabled for testing Device Card
        composable(Routes.LOGIN) {
            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {
                    navController.navigate(Routes.MAIN) {
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
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onLoginClicked = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        } */

        composable(Routes.DEVICE_CARD) {
            val deviceCardViewModel: DeviceCardViewModel = viewModel()

            DeviceCardScreen(
                viewModel = deviceCardViewModel
            )
        }
    }
}