package se.hkr.andriod.ui.screens.settings

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.hkr.andriod.data.mock.currentUser
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.screens.settings.components.SettingsItem
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun SettingsScreen(
    onLogoutClicked: () -> Unit
) {
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
            item { SettingsItem(title = "Language") { /* navigate */ } }
            item { SettingsItem(title = "Account Info") { /* navigate */ } }

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
                item { SettingsItem(title = "Users & Permissions") { /* navigate */ } }
            }
            if (currentUser.canViewDevices()) {
                item { SettingsItem(title = "Devices") { /* navigate */ } }
            }
            if (currentUser.canViewRooms()) {
                item { SettingsItem(title = "Rooms") { /* navigate */ } }
            }
            if (currentUser.canManageSchedules()) {
                item { SettingsItem(title = "Schedules") { /* navigate */ } }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

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
        }
    }
}
