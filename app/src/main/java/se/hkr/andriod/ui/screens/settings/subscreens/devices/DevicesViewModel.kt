package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val selectedRoomIdForDialog: String = ""
) {
    val selectedDevice: Device?
        get() = devices.firstOrNull { it.id == selectedDeviceId }

    fun getRoomName(roomId: String?): String? {
        if (roomId.isNullOrBlank()) return null
        return rooms.firstOrNull { it.id == roomId }?.name
    }

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
        roomStore.getRooms()
        deviceStore.fetchAllDeviceInfo()
        observeStores()
    }

    private fun observeStores() {
        // Observe devices
        viewModelScope.launch {
            deviceStore.allDevices.collect { devices ->
                _uiState.update { state ->
                    val selectedId = state.selectedDeviceId
                        .takeIf { id -> devices.any { it.id == id } }
                        ?: devices.firstOrNull()?.id.orEmpty()

                    val selectedDevice = devices.firstOrNull { it.id == selectedId }

                    state.copy(
                        devices = devices,
                        selectedDeviceId = selectedId,
                        selectedRoomIdForDialog = selectedDevice?.room.orEmpty()
                    )
                }
            }
        }

        // Observe rooms
        viewModelScope.launch {
            roomStore.rooms.collect { rooms ->
                _uiState.update { state ->
                    state.copy(rooms = rooms)
                }
            }
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
            val selectedDevice = state.selectedDevice ?: return@update state
            state.copy(
                showChangeRoomDialog = true,
                selectedRoomIdForDialog = selectedDevice.room.orEmpty()
            )
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
    }

    fun deleteSelectedDevice() {
        val selectedDevice = _uiState.value.selectedDevice ?: return

        deviceStore.deleteDevice(selectedDevice.id)

        dismissDialogs()
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
