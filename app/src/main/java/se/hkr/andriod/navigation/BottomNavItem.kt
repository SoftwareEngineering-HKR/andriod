package se.hkr.andriod.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Overview : BottomNavItem(
        route = Routes.DEVICE_OVERVIEW,
        label = "Devices",
        icon = Icons.Rounded.Home
    )

    object Management : BottomNavItem(
        route = Routes.DEVICE_MANAGEMENT,
        label = "Manage Devices",
        icon = Icons.Rounded.Widgets
    )

    object Settings : BottomNavItem(
        route = Routes.SETTINGS,
        label = "Settings",
        icon = Icons.Rounded.Settings
    )
}
