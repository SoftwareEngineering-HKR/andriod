package se.hkr.andriod.ui.screens.deviceoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AddDeviceBottomSheet
import se.hkr.andriod.ui.components.AppHomeTopBar
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun DeviceOverviewScreen() {
    var showAddSheet by remember { mutableStateOf(false) }
    // TODO: Get the real online and offline count fron devices
    val onlineCount = 3
    val offlineCount = 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        AppHomeTopBar(
            title = stringResource(R.string.device_overview),
            onlineCount = onlineCount,
            offlineCount = offlineCount,
            onAddClick = { showAddSheet = true }
        )

        // Main content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {}

        if (showAddSheet) {
            AddDeviceBottomSheet(
                onDismiss = { showAddSheet = false },
                onScanClick = { /* TODO: implement scan */ },
                onAddDeviceClick = { /* TODO: implement manual add */ },
                onCreateRoomClick = { /* TODO: implement create room */ }
            )
        }
    }
}
