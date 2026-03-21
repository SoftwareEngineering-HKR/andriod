package se.hkr.andriod.ui.screens.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.hkr.andriod.R
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.CustomScreenHeader
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun ScanScreen(
    viewModel: ScanViewModel,
    navController: NavController,
    onBackClick: () -> Unit
) {

    // Currently only contains the failed scan
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomScreenHeader(
                title = stringResource(R.string.scan_for_devices),
                onBackClick = onBackClick
            )

            FailedScanComponent(
                isRetrying = uiState.isRetrying,
                onRetry = { viewModel.onRetryClick() },
                onManualAdd = {
                    navController.navigate(Routes.ADD_DEVICE)
                }
            )

        }
    }

}