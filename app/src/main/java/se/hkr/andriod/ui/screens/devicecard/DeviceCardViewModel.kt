package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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
    private val _uiState = MutableStateFlow(DeviceScreenUiState())
    val uiState: StateFlow<DeviceScreenUiState> = _uiState

    fun toggleDevice(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isEnabled = enabled)
    }

    fun toggleSchedule() {
        // Will be implemented later
    }

    fun setDeviceData(
        deviceName: String,
        roomName: String,
        isOnline: Boolean,
        isEnabled: Boolean,
        icon: ImageVector,
        scheduleExpanded: Boolean,
        lastUpdatedText: String
    ) {
        _uiState.update {
            it.copy(
                deviceName = deviceName,
                roomName = roomName,
                isOnline = isOnline,
                isEnabled = isEnabled,
                icon = icon,
                scheduleExpanded = scheduleExpanded,
                lastUpdatedText = lastUpdatedText
            )
        }
    }
}