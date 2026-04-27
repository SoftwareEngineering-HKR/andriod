package se.hkr.andriod.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.hkr.andriod.R
import se.hkr.andriod.data.network.*
import se.hkr.andriod.data.network.AuthSession.getUser
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.components.AppButton
import se.hkr.andriod.ui.screens.settings.components.SettingsItem
import se.hkr.andriod.ui.screens.settings.components.ThemeSelectorDialog
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun SettingsScreen(
    navController: NavController,
    connectionManager: ConnectionManager,
    onLogoutClicked: () -> Unit
) {
    val context = LocalContext.current

    var showThemeDialog by remember { mutableStateOf(false) }

    val currentUser = getUser()

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
                    text = stringResource(R.string.settings_general),
                    modifier = Modifier.padding(bottom = 8.dp, top = 40.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Theme selector
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_theme)
                ) {
                    showThemeDialog = true
                }
            }

            // Language
            item {
                SettingsItem(
                    title = stringResource(R.string.language)
                ) { navController.navigate(Routes.LANGUAGE) }
            }

            // Account info
            item {
                SettingsItem(
                    title = stringResource(R.string.settings_account_info)
                ) { navController.navigate(Routes.ACCOUNT) }
            }

            // Household Settings
            if (currentUser.canShowHouseholdSettings()) {
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Text(
                        text = stringResource(R.string.settings_household),
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (currentUser.canManageUsers()) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.settings_users_devices)
                    ) { navController.navigate(Routes.USERS) }
                }
            }
            if (currentUser.canViewDevices()) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.settings_devices)
                    ) { navController.navigate(Routes.DEVICES) }
                }
            }
            if (currentUser.canViewRooms()) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.rooms)
                    ) { navController.navigate(Routes.ROOMS) }
                }
            }
            if (currentUser.canManageSchedules()) {
                item {
                    SettingsItem(
                        title = stringResource(R.string.settings_schedules)
                    ) { navController.navigate(Routes.SCHEDULES) }
                }
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
                        text = stringResource(R.string.logout),
                        onClick = {
                            val ip = connectionManager.getBackendIp()
                            val token = AuthSession.getToken()

                            // Always clear local state
                            val cookieJar = NetworkModule.getClient(context).cookieJar as PersistentCookieJar

                            fun finishLogout() {
                                AuthSession.clear(context)
                                cookieJar.clear()
                                connectionManager.disconnect()

                                android.os.Handler(android.os.Looper.getMainLooper()).post {
                                    onLogoutClicked()
                                }
                            }

                            if (ip == null || token == null) {
                                finishLogout()
                                return@AppButton
                            }

                            val authService = AuthService(context)

                            authService.logout(ip, token) { _, _ ->
                                finishLogout()
                            }
                        },
                        modifier = Modifier.width(160.dp)
                    )
                }
            }
        }

        // Theme selection dialog
        ThemeSelectorDialog(
            show = showThemeDialog,
            onDismiss = { showThemeDialog = false }
        )
    }
}
