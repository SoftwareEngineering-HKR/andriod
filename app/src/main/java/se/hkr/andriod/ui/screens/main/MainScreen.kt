package se.hkr.andriod.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.screens.devicemanagement.DeviceManagementScreen
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
import se.hkr.andriod.ui.screens.settings.subscreens.AccountInfoScreen
import se.hkr.andriod.ui.screens.settings.subscreens.DevicesScreen
import se.hkr.andriod.ui.screens.settings.subscreens.LanguageScreen
import se.hkr.andriod.ui.screens.settings.subscreens.RoomsScreen
import se.hkr.andriod.ui.screens.settings.subscreens.SchedulesScreen
import se.hkr.andriod.ui.screens.settings.subscreens.UsersScreen
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

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
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.lightBlue)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                containerColor = MaterialTheme.colorScheme.cardBackground
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
                DeviceOverviewScreen()
            }

            composable(Routes.DEVICE_MANAGEMENT) {
                DeviceManagementScreen()
            }

            navigation(
                startDestination = Routes.SETTINGS,
                route = "settings_graph"
            ) {

                composable(Routes.SETTINGS) {
                    SettingsScreen(
                        navController = navController,
                        onLogoutClicked = onLogout
                    )
                }

                composable(Routes.USERS) { UsersScreen() }
                composable(Routes.DEVICES) { DevicesScreen() }
                composable(Routes.ROOMS) { RoomsScreen() }
                composable(Routes.SCHEDULES) { SchedulesScreen() }
                composable(Routes.LANGUAGE) { LanguageScreen() }
                composable(Routes.ACCOUNT) { AccountInfoScreen() }
            }
        }
    }
}
