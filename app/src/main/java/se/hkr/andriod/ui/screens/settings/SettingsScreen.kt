package se.hkr.andriod.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.hkr.andriod.data.mock.currentUser
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.screens.settings.components.SettingsItem
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun SettingsScreen(
    navController: NavController,
    onLogoutClicked: () -> Unit
) {
    var discoveredIp: String? by remember { mutableStateOf("Not connected") }
    val connectionManager = remember { ConnectionManager() }

    // Temporary for testing of the deviceStore
    val devices by connectionManager.deviceStore.devices.collectAsState()
    LaunchedEffect(devices) {
        Log.d("DEVICE_STORE", "Current devices: $devices")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.lightBlue)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // General Settings
            item {
                Text(
                    text = "General Settings",
                    modifier = Modifier.padding(bottom = 8.dp, top = 40.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item { SettingsItem(title = "Theme") { /* show popup */ } }
            item { SettingsItem(title = "Language") { navController.navigate(Routes.LANGUAGE) } }
            item { SettingsItem(title = "Account Info") { navController.navigate(Routes.ACCOUNT) } }

            // Household Settings
            if (currentUser.canShowHouseholdSettings()) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Text(
                        text = "Household Settings",
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (currentUser.canManageUsers()) {
                item { SettingsItem(title = "Users & Permissions") { navController.navigate(Routes.USERS) } }
            }
            if (currentUser.canViewDevices()) {
                item { SettingsItem(title = "Devices") { navController.navigate(Routes.DEVICES) } }
            }
            if (currentUser.canViewRooms()) {
                item { SettingsItem(title = "Rooms") { navController.navigate(Routes.ROOMS) } }
            }
            if (currentUser.canManageSchedules()) {
                item { SettingsItem(title = "Schedules") { navController.navigate(Routes.SCHEDULES) } }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Logout button
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AppButton(
                        text = "Log Out",
                        onClick = onLogoutClicked,
                        modifier = Modifier.width(160.dp)
                    )
                }
            }

            // Connect to Server
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AppButton(
                        text = "Connect to Server",
                        onClick = {
                            discoveredIp = "Searching..."
                            connectionManager.startConnection { ip ->
                                discoveredIp = ip ?: "No backend found"
                            }
                        },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }

            // Backend IP display
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Backend IP: $discoveredIp",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            }

            // Test Message button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AppButton(
                        text = "Test Message",
                        onClick = {
                            val testDeviceId = "deviceTest2"
                            val testValue = 250

                            connectionManager.updateDeviceValue(testDeviceId, testValue)

                            Log.d(
                                "TEST_MESSAGE",
                                "Sent test update for device $testDeviceId : value $testValue"
                            )
                        },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }
    }
}
