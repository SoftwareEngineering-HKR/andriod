package se.hkr.andriod.navigation

import se.hkr.andriod.domain.model.device.Device

object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"

    const val MAIN = "main"

    const val DEVICE_OVERVIEW = "device_overview"
    const val ROOMS_OVERVIEW = "rooms_overview"
    const val SETTINGS = "settings"

    const val USERS = "users"
    const val DEVICES = "devices"
    const val ROOMS = "rooms"
    const val SCHEDULES = "schedules"
    const val LANGUAGE = "language"
    const val ACCOUNT = "account"


    const val DEVICE_CARD = "device_card/{type}/{id}"
    fun deviceCard(device: Device): String {
        return "device_card/${device.deviceTypeEnum.name}/${device.id}"
    }

    const val ROOM_DETAILS = "room_details/{roomName}"
    fun roomDetails(roomName: String): String {
        return "room_details/$roomName"
    }
}
