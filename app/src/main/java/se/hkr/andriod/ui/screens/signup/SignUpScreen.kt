package se.hkr.andriod.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.components.AppButton

sealed interface SignUpEvent {
    object SignUpClicked : SignUpEvent
    object GoToLoginClicked : SignUpEvent
}

@Composable
fun SignUpScreen(
    onEvent: (SignUpEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign Up Page")

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Sign Up",
            onClick = { onEvent(SignUpEvent.SignUpClicked) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            text = "Go to Login",
            onClick = { onEvent(SignUpEvent.GoToLoginClicked) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
