package se.hkr.andriod.ui.screens.roomsoverviewscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.hkr.andriod.R
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.AddDeviceBottomSheet
import se.hkr.andriod.ui.components.AppHomeTopBar
import se.hkr.andriod.ui.components.AppTextField
import se.hkr.andriod.ui.components.RoomCardItem
import se.hkr.andriod.ui.screens.main.goToRooms
import se.hkr.andriod.ui.screens.main.goToSchedules
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun RoomsOverviewScreen(
    navController: NavController,
    connectionManager: ConnectionManager
) {
    var showAddSheet by remember { mutableStateOf(false) }

    val devices by connectionManager.deviceStore.devices.collectAsState()

    val search = remember { mutableStateOf("") }

    val onlineCount = devices.count { it.online }
    val offlineCount = devices.count { !it.online }

    fun isSwitchDevice(device: Device): Boolean {
        return device.deviceTypeEnum in listOf(
            DeviceType.LIGHT,
            DeviceType.LOCK,
            DeviceType.FAN,
            DeviceType.SERVO,
            DeviceType.DOOR,
            DeviceType.WINDOW
        )
    }

    // Group only devices that belong to rooms
    val devicesByRoom = devices
        .filter {
            !it.room.isNullOrBlank() &&
                    it.room != "null"
        }
        .filter {
            val query = search.value.trim()

            it.room?.contains(query, ignoreCase = true) == true
        }
        .groupBy { it.room!! }

    val sortedRooms = devicesByRoom.keys.sorted()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        AppHomeTopBar(
            title = stringResource(R.string.rooms),
            onlineCount = onlineCount,
            offlineCount = offlineCount,
            onAddClick = { showAddSheet = true }
        )

        // Search bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.cardBackground
            )
        ) {
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

        // Rooms list
        if (devicesByRoom.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_rooms),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(sortedRooms) { room ->
                    val roomDevices = devicesByRoom[room] ?: emptyList()
                    val switchDevices = roomDevices.filter {
                        isSwitchDevice(it)
                    }

                    // Room switch is ON if any switch device is ON
                    val roomEnabled = switchDevices.any {
                        it.value > it.minValue
                    }

                    RoomCardItem(
                        roomName = room,
                        deviceCount = roomDevices.size,
                        enabled = switchDevices.isNotEmpty(),
                        checked = roomEnabled,
                        onClick = { navController.navigate(Routes.roomDetails(room)) },
                        onSwitchToggle = { turnOn ->
                            switchDevices.forEach { device ->
                                val value =
                                    if (turnOn) device.maxValue else device.minValue

                                connectionManager.updateDeviceValue(device.id, value)
                            }
                        },
                        elevation = 2.dp
                    )
                }

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
        }
    }
}
