package se.hkr.andriod.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.hkr.andriod.ui.screens.login.LoginScreen
import se.hkr.andriod.ui.screens.login.LoginViewModel
import se.hkr.andriod.ui.screens.main.MainScreen
import se.hkr.andriod.ui.screens.signup.SignUpScreen
import se.hkr.andriod.ui.screens.signup.SignUpViewModel
import se.hkr.andriod.ui.viewmodel.AuthViewModelFactory

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            val context = LocalContext.current
            val factory = remember { AuthViewModelFactory(context) }
            val loginViewModel: LoginViewModel = viewModel(factory = factory)

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
            val context = LocalContext.current
            val factory = remember { AuthViewModelFactory(context) }
            val registerViewModel: SignUpViewModel = viewModel(factory = factory)

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
        }
    }
}