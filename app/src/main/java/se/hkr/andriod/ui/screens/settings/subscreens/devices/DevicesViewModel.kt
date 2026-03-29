package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.Room

data class DevicesUiState(
    val devices: List<Device> = MockDevices.allDevices,
    val rooms: List<Room> = listOf(
        MockDevices.livingRoom,
        MockDevices.kitchen
    ),
    val selectedDeviceId: String =
        MockDevices.allDevices.firstOrNull()?.id.orEmpty(),

    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showChangeRoomDialog: Boolean = false,

    val inputText: String = "",
    val selectedRoomIdForDialog: String =
        MockDevices.allDevices.firstOrNull()?.room.orEmpty()
) {
    val selectedDevice: Device?
        get() = devices.firstOrNull() { it.id == selectedDeviceId }

    fun getRoomName(roomId: String?): String? {
        if (roomId.isNullOrBlank()) return null
        return rooms.firstOrNull { it.id == roomId }?.name
    }

    val selectedDeviceRoomName: String?
        get() = getRoomName(selectedDevice?.room)

    val availableRooms: List<Room>
        get() = rooms.sortedBy { it.name }

}

class DevicesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState: StateFlow<DevicesUiState> = _uiState

    fun onDeviceSelected(deviceId: String) {
        _uiState.update { state ->
            val updatedState = state.copy(selectedDeviceId = deviceId)

            val selectedDevice = updatedState.selectedDevice
                ?: return@update state

            updatedState.copy(
                selectedRoomIdForDialog = selectedDevice.room.orEmpty()
            )
        }
    }

    fun onInputChanged(value: String) {
        _uiState.value = _uiState.value.copy(inputText = value)
    }

    fun onRoomSelectedForDialog(roomId: String) {
        _uiState.update { it.copy(selectedRoomIdForDialog = roomId) }
    }

    fun showRenameDialog() {
        _uiState.update { state ->
            val selectedDevice = state.selectedDevice ?: return@update state
            state.copy(
                showRenameDialog = true,
                inputText = selectedDevice.displayName
            )
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun showChangeRoomDialog() {
        _uiState.update { state ->
            val selectedDevice = state.selectedDevice ?: return@update state
            state.copy(
                showChangeRoomDialog = true,
                selectedRoomIdForDialog = selectedDevice.room.orEmpty()
            )
        }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(
                showRenameDialog = false,
                showDeleteDialog = false,
                showChangeRoomDialog = false,
                inputText = ""
            )
        }
    }
}