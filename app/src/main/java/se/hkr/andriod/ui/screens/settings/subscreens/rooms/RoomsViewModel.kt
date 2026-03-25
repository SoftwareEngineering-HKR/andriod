package se.hkr.andriod.ui.screens.settings.subscreens.rooms

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