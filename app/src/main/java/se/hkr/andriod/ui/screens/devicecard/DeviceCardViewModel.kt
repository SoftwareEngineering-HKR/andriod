package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

data class DeviceScreenUiState(
    val deviceName: String = "Device Name",
    val roomName: String = "Room",
    val isOnline: Boolean = false,
    val isEnabled: Boolean = false,
    val icon: ImageVector = Icons.Rounded.Devices,
    val scheduleExpanded: Boolean = false,
    val lastUpdatedText: String = "Last updated 1 minute ago"
)

class DeviceCardViewModel : ViewModel() {

}