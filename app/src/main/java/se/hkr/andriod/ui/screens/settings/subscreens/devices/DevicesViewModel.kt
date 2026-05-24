package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.RoomStore
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.Room

data class DevicesUiState(
    val devices: List<Device> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val selectedDeviceId: String = "",
    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val showChangeRoomDialog: Boolean = false,
    val inputText: String = "",
    val isLoaded: Boolean = false,
    val selectedRoomIdForDialog: String = ""
) {
    val selectedDevice: Device?
        get() = devices.firstOrNull { it.id == selectedDeviceId }

    val availableRooms: List<Room>
        get() = rooms.sortedBy { it.name }
}

class DevicesViewModel(
    private val deviceStore: DeviceStore,
    private val roomStore: RoomStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DevicesUiState())
    val uiState: StateFlow<DevicesUiState> = _uiState

    init {
        viewModelScope.launch {
            roomStore.getRooms()
            deviceStore.fetchAllDeviceInfo()

            delay(500)

            loadSnapshot()
        }
    }

    private fun loadSnapshot() {
        val devices = deviceStore.allDevices.value
        val rooms = roomStore.rooms.value

        val selectedId = devices.firstOrNull()?.id.orEmpty()

        _uiState.update {
            it.copy(
                devices = devices,
                rooms = rooms,
                selectedDeviceId = selectedId,
                isLoaded = true
            )
        }
    }

    fun onDeviceSelected(deviceId: String) {
        _uiState.update { state ->
            val selectedDevice = state.devices.firstOrNull { it.id == deviceId }
                ?: return@update state

            state.copy(
                selectedDeviceId = deviceId,
                selectedRoomIdForDialog = selectedDevice.room.orEmpty()
            )
        }
    }

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(inputText = value) }
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
            val selectedDevice = state.selectedDevice
                ?: return@update state

            // Find room by matching the device room name
            val selectedRoomId = state.rooms
                .firstOrNull { it.name == selectedDevice.room }
                ?.id
                .orEmpty()

            state.copy(
                showChangeRoomDialog = true,
                selectedRoomIdForDialog = selectedRoomId
            )
        }
    }

    fun updateSelectedDeviceRoom() {
        val state = _uiState.value
        val selectedDevice = state.selectedDevice ?: return
        val selectedRoom = state.rooms.firstOrNull { it.id == state.selectedRoomIdForDialog }

        // Remove room
        if (selectedRoom == null) {
            deviceStore.updateDeviceRoom(
                deviceId = selectedDevice.id,
                roomId = "",
                roomName = null
            )
        } else {
            // Assign/change room
            deviceStore.updateDeviceRoom(
                deviceId = selectedDevice.id,
                roomId = selectedRoom.id,
                roomName = selectedRoom.name
            )
        }
        dismissDialogs()

        viewModelScope.launch {
            delay(200)
            loadSnapshot()
        }
    }

    fun renameSelectedDevice() {
        val state = _uiState.value
        val selectedDevice = state.selectedDevice ?: return
        val newName = state.inputText.trim()

        if (newName.isEmpty() || newName == selectedDevice.displayName) return

        // Use current description to avoid overwriting it
        val currentDescription = selectedDevice.description ?: ""

        deviceStore.updateDevice(
            deviceId = selectedDevice.id,
            name = newName,
            description = currentDescription
        )

        dismissDialogs()

        viewModelScope.launch {
            delay(200)
            loadSnapshot()
        }
    }

    fun deleteSelectedDevice() {
        val selectedDevice = _uiState.value.selectedDevice ?: return

        deviceStore.deleteDevice(selectedDevice.id)

        dismissDialogs()

        viewModelScope.launch {
            delay(200)
            loadSnapshot()
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
