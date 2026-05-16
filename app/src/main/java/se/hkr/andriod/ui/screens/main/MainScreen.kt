package se.hkr.andriod.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import se.hkr.andriod.navigation.Routes
import se.hkr.andriod.ui.screens.deviceoverview.DeviceOverviewScreen
import se.hkr.andriod.ui.screens.settings.SettingsScreen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import se.hkr.andriod.core.events.ErrorDispatcher
import se.hkr.andriod.data.language.LanguageStorage
import se.hkr.andriod.data.network.AuthSession
import se.hkr.andriod.data.network.NetworkModule
import se.hkr.andriod.data.network.PersistentCookieJar
import se.hkr.andriod.navigation.BottomNavItem
import se.hkr.andriod.ui.screens.devicecard.DeviceHostScreen
import se.hkr.andriod.ui.screens.roomdetails.RoomDetailsScreen
import se.hkr.andriod.ui.screens.roomsoverviewscreen.RoomsOverviewScreen
import se.hkr.andriod.ui.screens.settings.subscreens.accountinfo.AccountInfoScreen
import se.hkr.andriod.ui.screens.settings.subscreens.language.LanguageScreen
import se.hkr.andriod.ui.screens.settings.subscreens.rooms.RoomsScreen
import se.hkr.andriod.ui.screens.settings.subscreens.SchedulesScreen
import se.hkr.andriod.ui.screens.settings.subscreens.users.UsersScreen
import se.hkr.andriod.ui.screens.settings.subscreens.devices.DevicesScreen
import se.hkr.andriod.ui.screens.settings.subscreens.devices.DevicesViewModelFactory
import se.hkr.andriod.ui.screens.settings.subscreens.language.LanguageViewModel
import se.hkr.andriod.ui.screens.settings.subscreens.users.UsersViewModelFactory
import se.hkr.andriod.ui.screens.settings.subscreens.rooms.RoomsViewModelFactory
import se.hkr.andriod.ui.theme.cardBackground
import se.hkr.andriod.ui.theme.lightBlue

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    errorDispatcher: ErrorDispatcher
) {
    val mainViewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(errorDispatcher)
    )
    val connectionManager = mainViewModel.connectionManager
    val navController = rememberNavController()
    val context = LocalContext.current

    var hasFinishedInitialLoad by remember { mutableStateOf(false) }
    val devices by connectionManager.deviceStore.devices.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.initConnection(context)
    }

    // Force logout in auth failure (if token refresh fails)
    LaunchedEffect(Unit) {
        connectionManager.setOnAuthFailureListener {
            android.util.Log.d("AUTH", "Auth failed, forcing logout")

            val cookieJar = NetworkModule.getClient(context).cookieJar as PersistentCookieJar

            AuthSession.clear(context)
            cookieJar.clear()
            connectionManager.disconnect()
            onLogout() // navigate back to login
        }
    }

    LaunchedEffect(Unit) {
        if (devices.isNotEmpty()) {
            hasFinishedInitialLoad = true
        } else {
            snapshotFlow { devices }.first { it.isNotEmpty() }
            delay(500)
            hasFinishedInitialLoad = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.lightBlue)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                containerColor = MaterialTheme.colorScheme.cardBackground
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                listOf(
                    BottomNavItem.Overview,
                    BottomNavItem.Management,
                    BottomNavItem.Settings
                ).forEach { screen ->
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
                                contentDescription = stringResource(screen.labelRes)
                            )
                        },
                        label = { Text(stringResource(screen.labelRes)) },
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
                DeviceOverviewScreen(navController, connectionManager, hasFinishedInitialLoad)
            }

            composable(Routes.ROOMS_OVERVIEW) {
                RoomsOverviewScreen(navController, connectionManager)
            }

            composable(
                route = Routes.DEVICE_CARD,
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType },
                    navArgument("id") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val deviceId = backStackEntry.arguments?.getString("id") ?: ""
                val device = connectionManager.deviceStore.getDeviceById(deviceId)

                if (device == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    DeviceHostScreen(device, connectionManager, navController) { navController.navigateUp() }
                }
            }

            composable(
                route = Routes.ROOM_DETAILS,
                arguments = listOf(
                    navArgument("roomName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
                RoomDetailsScreen(navController, connectionManager, roomName)
            }

            navigation(
                startDestination = Routes.SETTINGS,
                route = "settings_graph"
            ) {

                composable(Routes.SETTINGS) {
                    SettingsScreen(
                        navController = navController,
                        connectionManager = connectionManager,
                        onLogoutClicked = onLogout
                    )
                }

                composable(Routes.USERS) {
                    UsersScreen(
                        viewModel(factory = UsersViewModelFactory(connectionManager.userStore, connectionManager.deviceStore)),
                        { navController.navigateUp() }
                    )
                }

                composable(Routes.DEVICES) {
                    DevicesScreen(
                        viewModel(factory = DevicesViewModelFactory(connectionManager.deviceStore, connectionManager.roomStore)),
                        { navController.navigateUp() }
                    )
                }

                composable(Routes.ROOMS) {
                    RoomsScreen(
                        viewModel(factory = RoomsViewModelFactory(connectionManager.roomStore, connectionManager.deviceStore)),
                        { navController.navigateUp() }
                    )
                }

                composable(Routes.SCHEDULES) { SchedulesScreen() }

                composable(Routes.LANGUAGE) {
                    val langContext = LocalContext.current.applicationContext
                    LanguageScreen(
                        viewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                return LanguageViewModel(LanguageStorage(langContext)) as T
                            }
                        }),
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable(Routes.ACCOUNT) {
                    AccountInfoScreen(viewModel(), { navController.navigateUp() })
                }
            }
        }
    }
}
