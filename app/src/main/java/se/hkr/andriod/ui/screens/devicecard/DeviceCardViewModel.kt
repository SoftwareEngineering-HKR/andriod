package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.domain.model.device.Device

// Template data
data class DeviceScreenUiState(
    val deviceId: String = "",
    val deviceName: String = "Unknown Device",
    val roomName: String = "Unknown Room",
    val isOnline: Boolean = true,
    val icon: ImageVector = Icons.Rounded.QuestionMark,
    val scheduleExpanded: Boolean = false,
    val lastUpdatedText: String = "Last updated 1 minute ago"
)

class DeviceCardViewModel(private val connectionManager: ConnectionManager? = null) : ViewModel() {

    private val _uiState = MutableStateFlow(DeviceScreenUiState())
    val uiState: StateFlow<DeviceScreenUiState> = _uiState

    // Set initial state from a device
    fun setDevice(device: Device) {
        _uiState.value = _uiState.value.copy(
            deviceId = device.id,
            deviceName = device.displayName,
            roomName = device.room ?: "Unknown Room",
            isOnline = device.online
        )
    }

    // Toggle UI switch
    fun toggleDevice(online: Boolean) {
        _uiState.value = _uiState.value.copy(isOnline = online)
    }

    // Update device value
    fun updateDeviceValue(device: Device, isOn: Boolean) {
        val value = if (isOn) device.maxValue else device.minValue
        connectionManager?.updateDeviceValue(device.id, value)
        // Optionally update local UI
        _uiState.value = _uiState.value.copy(isOnline = isOn)
    }

    // Schedule toggle
    fun toggleSchedule() {
        _uiState.value = _uiState.value.copy(scheduleExpanded = !_uiState.value.scheduleExpanded)
    }
}
