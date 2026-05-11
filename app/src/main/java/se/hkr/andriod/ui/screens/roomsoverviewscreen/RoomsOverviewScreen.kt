package se.hkr.andriod.ui.screens.roomsoverviewscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import se.hkr.andriod.R
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.ui.components.AddDeviceBottomSheet
import se.hkr.andriod.ui.components.AppHomeTopBar
import se.hkr.andriod.ui.screens.main.goToRooms
import se.hkr.andriod.ui.screens.main.goToSchedules
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun RoomsOverviewScreen(
    navController: NavController,
    connectionManager: ConnectionManager
) {
    var showAddSheet by remember { mutableStateOf(false) }

    val devices by connectionManager.deviceStore.devices.collectAsState()


    val onlineCount = devices.count { it.online }
    val offlineCount = devices.count { !it.online }

    Box(
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

        // Main content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {}

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
