package se.hkr.andriod.ui.screens.deviceoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.components.DeviceCardItem
import se.hkr.andriod.ui.screens.main.goToRooms
import se.hkr.andriod.ui.screens.main.goToSchedules
import se.hkr.andriod.ui.theme.cardBackground

@Composable
fun DeviceOverviewScreen(
    navController: NavController,
    connectionManager: ConnectionManager
) {
    var showAddSheet by remember { mutableStateOf(false) }
    var showScanModal by remember { mutableStateOf(false) }

    val devices by connectionManager.deviceStore.devices.collectAsState()

    val search = remember { mutableStateOf("") }

    val onlineCount = devices.count { it.online }
    val offlineCount = devices.count { !it.online }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        AppHomeTopBar(
            title = stringResource(R.string.nav_devices),
            onlineCount = onlineCount,
            offlineCount = offlineCount,
            onAddClick = { showAddSheet = true }
        )

        // Main content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.cardBackground)
        ) {
            // Search bar
            AppTextField(
            value = search.value,
            onValueChange = { search.value = it },
            placeholder = stringResource(R.string.search_placeholder),
            leadingIcon = Icons.Default.Search,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .offset(y = 10.dp)
            )
        }

        // Group devices by room, using "No Room" for unassigned
        val devicesByRoom = devices
            .filter { device ->
                val query = search.value.trim()

                device.displayName.contains(query, ignoreCase = true) ||
                        (device.room?.contains(query, ignoreCase = true) == true) ||
                        device.deviceTypeEnum.name.contains(query, ignoreCase = true)
            }
            .groupBy { device ->
                val room = device.room
                if (room.isNullOrBlank() || room == "null") "No Room" else room
            }

        // Sort rooms alphabetically, but put "No Room" last
        val sortedRooms = devicesByRoom.keys
            .filter { it != "No Room" }.sorted() +
                devicesByRoom.keys.filter { it == "No Room" }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            sortedRooms.forEach { room ->
                // Room header
                item {
                    Text(
                        text = room,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Devices in this room
                items(devicesByRoom[room]!!) { device ->
                    DeviceCardItem(
                        device = device,
                        onClick = { navController.navigate(Routes.deviceCard(device)) },
                        onSwitchToggle = { isOn ->
                            val value = if (isOn) device.maxValue else device.minValue
                            connectionManager.updateDeviceValue(device.id, value)
                        },
                        onAction = {
                            connectionManager.deviceStore.updateDeviceValue(
                                device.id,
                                device.minValue
                            )
                        },
                        elevation = 2.dp
                    )
                }

                // Space after each room section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Bottom sheet for add options
        Box {
            if (showAddSheet) {
                AddDeviceBottomSheet(
                    onDismiss = { showAddSheet = false },
                    onSchedulesClick = {
                        showAddSheet = false
                        navController.goToSchedules()
                    },
                    onCreateRoomClick = {
                        showAddSheet = false
                        navController.goToRooms()
                    }
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
