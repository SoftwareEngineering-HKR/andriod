package se.hkr.andriod.ui.screens.settings.subscreens.rooms

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

data class RoomsUiState(
    val rooms: List<Room> = emptyList(),
    val selectedRoomId: String = "",
    val allDevices: List<Device> = emptyList(),
    val devicesInRoom: List<Device> = emptyList(),
    val availableDevices: List<Device> = emptyList(),
    val showCreateDialog: Boolean = false,
    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val inputText: String = "",
    val isLoaded: Boolean = false
) {
    val selectedRoom: Room?
        get() = rooms.firstOrNull { it.id == selectedRoomId }
}

class RoomsViewModel(
    private val roomStore: RoomStore,
    private val deviceStore: DeviceStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState: StateFlow<RoomsUiState> = _uiState

    init {
        viewModelScope.launch {
            roomStore.getRooms()
            deviceStore.fetchAllDeviceInfo()

            delay(500)

            loadSnapshot()
        }
    }

    private fun loadSnapshot() {
        val rooms = roomStore.rooms.value
        val devices = deviceStore.allDevices.value

        val selectedRoomId = _uiState.value.selectedRoomId
            .takeIf { id -> rooms.any { it.id == id } }
            ?: rooms.firstOrNull()?.id.orEmpty()

        val selectedRoom = rooms.firstOrNull { it.id == selectedRoomId }

        _uiState.update {
            it.copy(
                rooms = rooms,
                selectedRoomId = selectedRoomId,
                allDevices = devices,
                devicesInRoom = devices.filter { device ->
                    device.room == selectedRoom?.name
                },
                availableDevices = devices.filter { device ->
                    device.room != selectedRoom?.name
                },
                isLoaded = true
            )
        }
    }

    fun onRoomSelected(roomId: String) {
        _uiState.update { state ->

            val selectedRoom = state.rooms.firstOrNull { it.id == roomId }
                ?: return@update state

            state.copy(
                selectedRoomId = roomId,
                devicesInRoom = state.allDevices.filter {
                    it.room == selectedRoom.name
                },
                availableDevices = state.allDevices.filter {
                    it.room != selectedRoom.name
                }
            )
        }
    }

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(inputText = value) }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true, inputText = "") }
    }

    fun showRenameDialog() {
        _uiState.update { state ->
            state.copy(
                showRenameDialog = true,
                inputText = state.selectedRoom?.name ?: ""
            )
        }
    }

    fun showDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = true) }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(
                showCreateDialog = false,
                showRenameDialog = false,
                showDeleteDialog = false,
                inputText = ""
            )
        }
    }

    fun createRoom() {
        val name = _uiState.value.inputText.trim()

        if (name.isBlank()) return

        viewModelScope.launch {
            roomStore.createRoom(name)
            dismissDialogs()
            delay(200)
            loadSnapshot()
        }
    }

    fun renameRoom() {
        val state = _uiState.value
        val room = state.selectedRoom ?: return
        val newName = state.inputText.trim()

        if (newName.isBlank() || newName == room.name) return

        viewModelScope.launch {
            roomStore.updateRoomName(room.id, newName)
            dismissDialogs()
            delay(200)
            loadSnapshot()
        }
    }

    fun deleteRoom() {
        val room = _uiState.value.selectedRoom ?: return

        viewModelScope.launch {
            roomStore.deleteRoom(room.id)
            dismissDialogs()
            delay(200)
            loadSnapshot()
        }
    }

    fun addDeviceToRoom(device: Device) {
        val selectedRoom = _uiState.value.selectedRoom ?: return

        deviceStore.updateDeviceRoom(
            deviceId = device.id,
            roomId = selectedRoom.id,
            roomName = selectedRoom.name
        )

        viewModelScope.launch {
            delay(200)
            loadSnapshot()
        }
    }

    fun removeDeviceFromRoom(device: Device) {
        deviceStore.updateDeviceRoom(
            deviceId = device.id,
            roomId = "",
            roomName = null
        )

        viewModelScope.launch {
            delay(200)
            loadSnapshot()
        }
    }
}
