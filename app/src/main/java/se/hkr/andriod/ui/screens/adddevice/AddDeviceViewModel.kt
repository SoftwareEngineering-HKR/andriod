package se.hkr.andriod.ui.screens.adddevice

import androidx.lifecycle.ViewModel
import se.hkr.andriod.navigation.DeviceType

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

class AddDeviceViewModel : ViewModel() {

}