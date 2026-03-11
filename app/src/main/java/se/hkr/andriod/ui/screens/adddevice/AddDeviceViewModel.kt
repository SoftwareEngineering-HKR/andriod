package se.hkr.andriod.ui.screens.adddevice

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.navigation.DeviceType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Lock
import se.hkr.andriod.R

data class AddDeviceUiState(
    val deviceName: String = "",

    // Mockup Data, real data will come from backend
    val rooms: List<String> = listOf(
        "Living Room",
        "Bedroom"
    ),

    val selectedRoom: String? = null,
    val selectedDeviceType: DeviceType? = null,
    val connectionType: ConnectionType? = null,
    val error: String? = null
)

enum class ConnectionType {
    WIFI,
    BLUETOOTH
}

data class DeviceTypeItem(
    val type: DeviceType,
    val icon: ImageVector,
    @StringRes val labelRes: Int
)

class AddDeviceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AddDeviceUiState())
    val uiState: StateFlow<AddDeviceUiState> = _uiState

    fun onDeviceNameChanged(deviceName: String) {
        _uiState.update {
            it.copy(deviceName = deviceName)
        }
    }

    fun onRoomSelected(room: String) {
        _uiState.update {
            it.copy(selectedRoom = room)
        }
    }

    fun addRoom(room: String) {
        _uiState.update {
            it.copy(rooms = it.rooms + room,
                selectedRoom = room)
        }
    }

    fun onDeviceTypeSelected(deviceType: DeviceType) {
        _uiState.update {
            it.copy(selectedDeviceType = deviceType)
        }
    }

    fun onConnectionTypeSelected(connectionType: ConnectionType) {
        _uiState.update {
            it.copy(connectionType = connectionType)
        }
    }

    fun onAddDeviceClick() {
        val state = _uiState.value

        if (
            state.deviceName.isBlank() ||
            state.selectedRoom == null ||
            state.selectedDeviceType == null ||
            state.connectionType == null
        ) {
            _uiState.update {
                it.copy(error = "Please fill in all fields")
            }
            return
        }

        // TODO: Save Device and Room + set up connection with device
    }

    // TODO: Add more devices
    val deviceTypes = listOf(
        DeviceTypeItem(
            DeviceType.LIGHT,
            Icons.Rounded.Lightbulb,
            labelRes = R.string.device_type_light
        ),
        DeviceTypeItem(
            DeviceType.LOCK,
            Icons.Rounded.Lock,
            labelRes = R.string.device_type_lock
        )
    )
}