package se.hkr.andriod.ui.screens.settings.subscreens.rooms

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.RoomStore

// Currently only mock data, will change
data class RoomsUiState(
    val rooms: List<String> = listOf(
        "Living Room",
        "Bedroom"
    ),
    val selectedRoom: String = "Living Room",

    val devicesInRoom: List<String> = listOf(
        "Living Room Light",
        "Door Lock"
    ),

    val availableDevices: List<String> = listOf(
        "New Light",
        "New Lock"
    ),

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

    fun onRoomSelected(room: String) {
        _uiState.value = _uiState.value.copy(selectedRoom = room)
    }

    fun onInputChanged(value: String) {
        _uiState.value = _uiState.value.copy(inputText = value)
    }

    fun showCreateDialog() {
        _uiState.value = _uiState.value.copy(showCreateDialog = true, inputText = "")
    }

    fun showRenameDialog() {
        _uiState.update {
            it.copy(
                showRenameDialog = true,
                inputText = it.selectedRoom ?: ""
            )
        }
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
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
}