package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

class DeviceCardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DeviceScreenUiState())
    val uiState: StateFlow<DeviceScreenUiState> = _uiState

    // Change template data to device specific data
    fun setTemplateState(state: DeviceScreenUiState) {
        _uiState.value = state
    }

    // Turn device on/off
    fun toggleDevice(online: Boolean) {
        _uiState.value = _uiState.value.copy(isOnline = online)
    }

    // Schedule
    fun toggleSchedule() {
        _uiState.value = _uiState.value.copy(scheduleExpanded = !_uiState.value.scheduleExpanded)

    }

}