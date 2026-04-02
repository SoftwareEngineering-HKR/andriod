package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.domain.model.device.Room

data class DeviceInfoUi(
    val name: String,
    val type: String,
    val room: String,
    val status: String,
    val description: String,
    val value: String,
    val isOnline: Boolean
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

    val deviceType = when (device?.deviceTypeEnum) {
        DeviceType.LIGHT -> stringResource(R.string.device_type_light)
        DeviceType.LOCK -> stringResource(R.string.device_type_lock)
        DeviceType.SENSOR -> stringResource(R.string.device_type_sensor)
        null -> notAvailable
        else -> notAvailable // TODO Change for remaining device types (Gas, Fan, Window, Humidity)
    }

    val deviceStatus = when(device?.online) {
        true -> stringResource(R.string.online)
        false -> stringResource(R.string.offline)
        null -> notAvailable
    }

    val deviceValue = when (device?.deviceTypeEnum) {

        DeviceType.LIGHT -> {
            if (device.maxValue <= 1) {
                if (device.value > 0) {
                    stringResource(R.string.device_state_on)
                } else {
                    stringResource(R.string.device_state_off)
                }
            } else {
                if (!device.scaleName.isNullOrBlank()) {
                    stringResource(R.string.device_value_with_scale, device.value, device.scaleName ?: "")
                } else {
                    device.value.toString()
                }
            }
        }

        DeviceType.LOCK -> {
            if (device.value > 0) {
                stringResource(R.string.device_state_unlocked)
            } else {
                stringResource(R.string.device_state_locked)
            }
        }

        DeviceType.SENSOR -> {
            if (!device.scaleName.isNullOrBlank()) {
                stringResource(R.string.device_value_with_scale, device.value, device.scaleName ?: "")
            } else {
                device.value.toString()
            }
        }

        null -> notAvailable
        // TODO Change for remaining device types (Gas, Fan, Window, Humidity)
        else -> {
            if (!device.scaleName.isNullOrBlank()) {
                stringResource(
                    R.string.device_value_with_scale,
                    device.value,
                    device.scaleName ?: ""
                )
            } else {
                device.value.toString()
            }
        }
    }

    return DeviceInfoUi(
        name = device?.displayName ?: notAvailable,
        type = deviceType,
        room = roomName,
        status = deviceStatus,
        description = device?.description?.takeIf {
            it.isNotBlank() && it.lowercase() != "null"
        } ?: noDescription,
        value = deviceValue,
        isOnline = device?.online == true
    )
}
