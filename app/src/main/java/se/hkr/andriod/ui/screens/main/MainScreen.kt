package se.hkr.andriod.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.screens.devicemanagement.DeviceManagementScreen
import se.hkr.andriod.ui.screens.deviceoverview.DeviceOverviewEvent
import se.hkr.andriod.ui.screens.deviceoverview.DeviceOverviewScreen
import se.hkr.andriod.ui.screens.settings.SettingsScreen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import se.hkr.andriod.navigation.BottomNavItem

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Overview,
        BottomNavItem.Management,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))

            ) {
                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent
                        ),
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any {
                            it.route == screen.route
                        } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Routes.DEVICE_OVERVIEW,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Routes.DEVICE_OVERVIEW) {
                // Temporary and will be removed once the DeviceOverviewScreen is implemented.
                DeviceOverviewScreen { event ->
                    when (event) {
                        DeviceOverviewEvent.LogOutClicked -> {
                            onLogout()
                        }
                    }
                }
            }

            composable(Routes.DEVICE_MANAGEMENT) {
                DeviceManagementScreen()
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onLogoutClicked = onLogout
                )
            }
        }
    }
}
