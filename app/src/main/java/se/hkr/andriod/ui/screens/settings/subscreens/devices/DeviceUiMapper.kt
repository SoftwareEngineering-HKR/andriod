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
): DeviceInfoUi {
    val notAvailable = stringResource(R.string.not_available)
    val noRoomAssigned = stringResource(R.string.no_room_assigned)
    val noDescription = stringResource(R.string.no_description)

    val roomName = device?.room?.takeIf { it.isNotBlank() } ?: noRoomAssigned

    val deviceType = when (device?.deviceTypeEnum) {
        DeviceType.LIGHT -> stringResource(R.string.device_type_light)
        DeviceType.LOCK -> stringResource(R.string.device_type_lock)
        DeviceType.SENSOR -> stringResource(R.string.device_type_sensor)
        DeviceType.GAS -> stringResource(R.string.device_type_gas)
        DeviceType.HUMIDITY -> stringResource(R.string.device_type_humidity)
        DeviceType.STEAM -> stringResource(R.string.device_type_steam)
        DeviceType.BUZZ -> stringResource(R.string.device_type_buzz)
        DeviceType.FAN -> stringResource(R.string.device_type_fan)
        DeviceType.SERVO -> stringResource(R.string.device_type_servo)
        DeviceType.WINDOW -> stringResource(R.string.device_type_window)
        DeviceType.DOOR -> stringResource(R.string.device_type_door)
        DeviceType.DISPLAY -> stringResource(R.string.device_type_display)
        DeviceType.UNKNOWN, null -> notAvailable
    }

    val deviceValue = device?.let { d ->
        when (d.deviceTypeEnum) {
            DeviceType.LIGHT -> {
                if (d.maxValue > 0) {
                    val brightnessPercent = (d.value.toFloat() / d.maxValue.toFloat() * 100).toInt()
                    stringResource(R.string.device_value_percent, brightnessPercent)
                } else {
                    stringResource(R.string.device_state_off)
                }
            }

            DeviceType.SENSOR,
            DeviceType.GAS,
            DeviceType.HUMIDITY,
            DeviceType.STEAM -> {
                if (!d.scaleName.isNullOrBlank()) {
                    stringResource(R.string.device_value_with_scale, d.value, d.scaleName!!)
                } else {
                    d.value.toString()
                }
            }

            else -> { // On/Off devices
                if (d.value > d.minValue) {
                    stringResource(R.string.device_state_on)
                } else {
                    stringResource(R.string.device_state_off)
                }
            }
        }
    } ?: notAvailable

    val deviceStatus = when (device?.online) {
        true -> stringResource(R.string.online)
        false -> stringResource(R.string.offline)
        null -> notAvailable
    }

    return DeviceInfoUi(
        name = device?.displayName ?: notAvailable,
        type = deviceType,
        room = roomName,
        status = deviceStatus,
        description = device?.description?.takeIf { it.isNotBlank() && it.lowercase() != "null" } ?: noDescription,
        value = deviceValue,
        isOnline = device?.online == true
    )
}
