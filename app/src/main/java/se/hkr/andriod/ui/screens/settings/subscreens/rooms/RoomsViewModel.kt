package se.hkr.andriod.ui.screens.settings.subscreens.rooms

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

data class RoomsUiState(
    val rooms: List<Room> = emptyList(),
    val selectedRoom: Room? = null,
    val allDevices: List<Device> = emptyList(),
    val devicesInRoom: List<Device> = emptyList(),
    val availableDevices: List<Device> = emptyList(),
    val showCreateDialog: Boolean = false,
    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val inputText: String = ""
)

class RoomsViewModel(
    private val roomStore: RoomStore,
    private val deviceStore: DeviceStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoomsUiState())
    val uiState: StateFlow<RoomsUiState> = _uiState

    init {
        roomStore.getRooms()
        deviceStore.fetchAllDeviceInfo()
        observeStores()
    }

    private fun observeStores() {
        viewModelScope.launch {
            roomStore.rooms.collect { rooms ->
                _uiState.update { state ->
                    val selected = state.selectedRoom ?: rooms.firstOrNull()
                    val inRoom = state.allDevices.filter { it.room == selected?.name }
                    val available = state.allDevices.filter { it.room != selected?.name }

                    state.copy(
                        rooms = rooms,
                        selectedRoom = selected,
                        devicesInRoom = inRoom,
                        availableDevices = available
                    )
                }
            }
        }

        viewModelScope.launch {
            deviceStore.allDevices.collect { devices ->
                _uiState.update { state ->
                    val room = state.selectedRoom
                    state.copy(
                        allDevices = devices,
                        devicesInRoom = devices.filter { it.room == room?.name },
                        availableDevices = devices.filter { it.room != room?.name }
                    )
                }
            }
        }
    }

    fun onRoomSelected(room: Room) {
        _uiState.update { state ->
            state.copy(
                selectedRoom = room,
                devicesInRoom = state.allDevices.filter { it.room == room.name },
                availableDevices = state.allDevices.filter { it.room != room.name }
            )
        }
    }

    fun devicesInSelectedRoom(): List<Device> = _uiState.value.devicesInRoom

    fun availableDevices(): List<Device> = _uiState.value.availableDevices

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(inputText = value) }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true, inputText = "") }
    }

    fun showRenameDialog() {
        _uiState.update {
            it.copy(
                showRenameDialog = true,
                inputText = it.selectedRoom?.name ?: ""
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

    // TODO
    fun createRoom() {}

    fun renameRoom() {}

    fun deleteRoom() {}

    // TODO: implement when backend supports adding/removing devices to/from rooms
    fun addDeviceToRoom(device: Device) {}

    fun removeDeviceFromRoom(device: Device) {}
}