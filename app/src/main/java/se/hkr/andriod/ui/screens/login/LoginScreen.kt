package se.hkr.andriod.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue
import se.hkr.andriod.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    if (uiState.navigateToHome) {
        LaunchedEffect(Unit) {
            onNavigateToHome()
            viewModel.onNavigationDone()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.cardBackground),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).padding(top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Home,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        text = stringResource(R.string.login_subtitle),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        label = stringResource(R.string.email_label),
                        value = uiState.email,
                        onValueChange = { viewModel.onEmailChanged(it) },
                        placeholder = stringResource(R.string.email_placeholder),
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
                        errorText = uiState.emailError?.let { stringResource(it) }
                    )

                    AppTextField(
                        label = stringResource(R.string.password_label),
                        value = uiState.password,
                        onValueChange = { viewModel.onPasswordChanged(it) },
                        placeholder = stringResource(R.string.password_placeholder),
                        isPassword = true,
                        errorText = uiState.passwordError?.let { stringResource(it) }
                    )

                    AppButton(
                        text = stringResource(R.string.sign_in_button),
                        loadingText = stringResource(R.string.signing_in),
                        icon = Icons.Rounded.Lock,
                        onClick = { viewModel.onLoginClicked() },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        isLoading = uiState.isLoading
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = stringResource(R.string.dont_have_account_text))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.sign_up_button),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { onSignUpClicked() }
                        )
                    }
                }
            }
        }
    }
}
