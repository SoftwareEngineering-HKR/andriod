package se.hkr.andriod.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.core.events.ErrorDispatcher
import se.hkr.andriod.ui.theme.inputFieldFill

@Composable
fun GlobalErrorSnackbarHost(
    errorDispatcher: ErrorDispatcher
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorDispatcher) {
        errorDispatcher.errors.collect { error ->
            snackbarHostState.showSnackbar(error.message)
        }
    }

    Box(Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = MaterialTheme.colorScheme.inputFieldFill,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
