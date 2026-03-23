package se.hkr.andriod.ui.screens.deviceoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import se.hkr.andriod.R
import se.hkr.andriod.ui.components.AddDeviceBottomSheet
import se.hkr.andriod.ui.components.AppHomeTopBar
import se.hkr.andriod.ui.components.ScanDevicesModal
import se.hkr.andriod.ui.theme.lightBlue
import androidx.navigation.NavController
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.components.DeviceCardItem
import se.hkr.andriod.ui.components.DeviceItemModel

@Composable
fun DeviceOverviewScreen(navController: NavController) {
    var showAddSheet by remember { mutableStateOf(false) }
    var showScanModal by remember { mutableStateOf(false) }

    val devices = MockDevices.allDevices
    val search = remember { mutableStateOf("") }

    // Todo: use the real offline/online count
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
        // Search bar Todo: Implement functionality
        AppTextField(
            value = search.value,
            onValueChange = { search.value = it },
            placeholder = "Search here...",
            leadingIcon = Icons.Default.Search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Scrollable list of device cards
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(devices) { device ->
                val icon = when (device.deviceTypeEnum) {
                    DeviceType.LIGHT -> Icons.Default.Lightbulb
                    DeviceType.LOCK -> Icons.Default.Lock
                    DeviceType.SENSOR -> Icons.Default.Sensors
                }

                DeviceCardItem(
                    device = DeviceItemModel(
                        id = device.id.toString(),
                        name = device.displayName,
                        room = when (device.room) { // Temporary hard coding until RoomStore is implemented.
                            MockDevices.livingRoom.id -> "Living Room"
                            MockDevices.kitchen.id -> "Kitchen"
                            else -> "Unknown Room"
                        },
                        isOnline = device.online,
                        icon = icon,
                    ),
                    onClick = {
                        navController.navigate(Routes.deviceCard(device))
                    },
                    onSwitchToggle = { /* Todo: Add functionality */ },
                    elevation = 2.dp
                )
            }
        }

        // Bottom sheet for add options
        Box {
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
