package se.hkr.andriod.ui.screens.settings.subscreens.devices

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

    val showCreateDialog: Boolean = false,
    val showRenameDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,

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