package se.hkr.andriod.ui.screens.deviceoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AddDeviceBottomSheet
import se.hkr.andriod.ui.components.AppHomeTopBar
import se.hkr.andriod.ui.components.ScanDevicesModal
import se.hkr.andriod.ui.theme.lightBlue
import androidx.navigation.NavController
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.navigation.Routes

@Composable
fun DeviceOverviewScreen(navController: NavController) {
    var showAddSheet by remember { mutableStateOf(false) }
    var showScanModal by remember { mutableStateOf(false) }

    val onlineCount = 3
    val offlineCount = 1

    Column(
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
        // Temporary buttons for the lock and light
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.deviceCard(DeviceType.LOCK))
                    }
                ) {
                    Text("Open Lock")
                }

                Button(
                    onClick = {
                        navController.navigate(Routes.deviceCard(DeviceType.LIGHT))
                    }
                ) {
                    Text("Open Light")
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            // Bottom sheet for add options
            if (showAddSheet) {
                AddDeviceBottomSheet(
                    onDismiss = { showAddSheet = false },
                    onScanClick = {
                        showAddSheet = false
                        showScanModal = true
                    },
                    // Todo: Fix the scan and add menus + navigate correctly
                    onAddDeviceClick = {
                        showAddSheet = false
                        navController.navigate(Routes.SCAN)
                    },
                    onCreateRoomClick = { /* TODO: implement create room */ }
                )
            }

            // Scan modal overlay
            if (showScanModal) {
                ScanDevicesModal(
                    onDismiss = { showScanModal = false }
                )
            }
        }
    }
}
