package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class DeviceScreenUiState(
    val deviceName: String = "Device Name",
    val roomName: String = "Room",
    val isOnline: Boolean = true,
    val icon: ImageVector = Icons.Rounded.QuestionMark,
    val scheduleExpanded: Boolean = false,
    val lastUpdatedText: String = "Last updated 1 minute ago"
)

class DeviceCardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DeviceScreenUiState())
    val uiState: StateFlow<DeviceScreenUiState> = _uiState

    private val _components = MutableStateFlow<List<DeviceComponent>>(emptyList())
    val components: StateFlow<List<DeviceComponent>> = _components

    fun setTemplateState(state: DeviceScreenUiState) {
        _uiState.value = state
    }

    fun toggleDevice(online: Boolean) {
        _uiState.value = _uiState.value.copy(isOnline = online)
    }

    fun toggleSchedule() {
        // Will be implemented later
    }

}