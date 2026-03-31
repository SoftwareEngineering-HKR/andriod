package se.hkr.andriod.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import se.hkr.andriod.R

sealed class BottomNavItem(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    object Overview : BottomNavItem(
        route = Routes.DEVICE_OVERVIEW,
        labelRes = R.string.nav_devices,
        icon = Icons.Rounded.Home
    )

    object Management : BottomNavItem(
        route = Routes.DEVICE_MANAGEMENT,
        labelRes = R.string.nav_manage_devices,
        icon = Icons.Rounded.Widgets
    )

    object Settings : BottomNavItem(
        route = Routes.SETTINGS,
        labelRes = R.string.nav_settings,
        icon = Icons.Rounded.Settings
    )
}