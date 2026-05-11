package se.hkr.andriod.ui.screens.roomdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.ui.components.DeviceCardItem
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun RoomDetailsScreen(
    navController: NavController,
    connectionManager: ConnectionManager,
    roomName: String
) {
    val devices by connectionManager.deviceStore.devices.collectAsState()
    val roomDevices = devices.filter {
        it.room == roomName
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 40.dp, bottom = 12.dp)
        ) {
            Text(
                text = roomName,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${roomDevices.size} devices",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(roomDevices) { device ->
                DeviceCardItem(
                    device = device,
                    // No navigation into device page
                    onClick = null,

                    onSwitchToggle = { isOn ->
                        val value =
                            if (isOn)
                                device.maxValue
                            else
                                device.minValue

                        connectionManager.updateDeviceValue(
                            device.id,
                            value
                        )
                    },

                    onAction = {
                        connectionManager.deviceStore.updateDeviceValue(
                            device.id,
                            device.minValue.toString()
                        )
                    },
                    elevation = 2.dp
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
