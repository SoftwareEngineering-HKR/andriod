package se.hkr.andriod.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.components.AppButton

sealed interface LoginEvent {
    object LoginClicked : LoginEvent
    object SignUpClicked : LoginEvent
}

@Composable
fun LoginScreen(
    onEvent: (LoginEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login Page")

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Log In",
            onClick = { onEvent(LoginEvent.LoginClicked) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            text = "Go to Sign Up",
            onClick = { onEvent(LoginEvent.SignUpClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
