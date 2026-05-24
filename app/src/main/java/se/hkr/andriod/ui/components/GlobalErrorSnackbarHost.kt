package se.hkr.andriod.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import se.hkr.andriod.core.events.ErrorDispatcher
import se.hkr.andriod.core.localization.ErrorMessageMapper
import se.hkr.andriod.ui.theme.inputFieldFill

@SuppressLint("LocalContextResourcesRead")
@Composable
fun GlobalErrorSnackbarHost(
    errorDispatcher: ErrorDispatcher
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val resources = remember(context) { context.resources }

    LaunchedEffect(errorDispatcher) {
        errorDispatcher.errors.collect { error ->
            val messageResId = ErrorMessageMapper.map(error.message)
            val message = resources.getString(messageResId)
            snackbarHostState.showSnackbar(message)
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
