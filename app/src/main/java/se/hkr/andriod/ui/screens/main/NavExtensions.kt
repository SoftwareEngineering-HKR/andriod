package se.hkr.andriod.ui.screens.main

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import se.hkr.andriod.navigation.BottomNavItem
import se.hkr.andriod.navigation.Routes

fun NavController.goToRooms() {
    // Switch to Settings tab
    navigate(BottomNavItem.Settings.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    // Then go to Rooms screen
    navigate(Routes.ROOMS)
}

fun NavController.goToSchedules() {
    // Switch to Settings tab
    navigate(BottomNavItem.Settings.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
    // Then go to Schedules screen
    navigate(Routes.SCHEDULES)
}
