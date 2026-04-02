package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.Room

data class DeviceInfoUi(
    val name: String,
    val type: String,
    val room: String,
    val status: String,
    val ip: String,
    val description: String,
    val value: String,
    val id: String
)

@Composable
fun mapDeviceToInfoUi(
    device: Device?,
    rooms: List<Room>
) : DeviceInfoUi {
    val notAvailable = stringResource(R.string.not_available)
    val noRoomAssigned = stringResource(R.string.no_room_assigned)
    val noDescription = stringResource(R.string.no_description)

    val roomName = rooms.firstOrNull { it.id == device?.room }?.name ?: noRoomAssigned

    val deviceType = device?.deviceTypeEnum
        ?.name
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: notAvailable

    val deviceStatus = when(device?.online) {
        true -> stringResource(R.string.online)
        false -> stringResource(R.string.offline)
        null -> notAvailable
    }

    val deviceValue = when{
        device == null -> notAvailable
        !device.scaleName.isNullOrBlank() -> {
            stringResource(
                R.string.device_value_with_scale,
                device.value,
                device.scaleName ?: ""
            )
        }
        else -> device.value.toString()
    }

    return DeviceInfoUi(
        name = device?.displayName ?: notAvailable,
        type = deviceType,
        room = roomName,
        status = deviceStatus,
        ip = device?.ip?.takeIf { it.isNotBlank() && it.lowercase() != "null"} ?: notAvailable,
        description = device?.description?.takeIf {
            it.isNotBlank() && it.lowercase() != "null"
        } ?: noDescription,
        value = deviceValue,
        id = device?.id ?: notAvailable
    )
}
