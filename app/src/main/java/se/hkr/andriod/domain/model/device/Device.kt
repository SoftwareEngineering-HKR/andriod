package se.hkr.andriod.domain.model.device

import org.json.JSONObject
import se.hkr.andriod.domain.model.user.User

data class Device(
    val id: String,
    val room: String?,
    val type: String,
    val online: Boolean,
    val ip: String?,
    var name: String?,
    var description: String?,
    var value: Int = 0,
    var maxValue: Int = 1,
    var minValue: Int = 0,
    var scaleName: String? = null,
    val users: List<User> = emptyList()
) {
    // Computed property for UI
    val deviceTypeEnum: DeviceType
        get() = when(type.lowercase()) {
            "light" -> DeviceType.LIGHT
            "lock" -> DeviceType.LOCK
            "gas" -> DeviceType.GAS
            "steam" -> DeviceType.STEAM
            "humidity" -> DeviceType.HUMIDITY
            "buzz" -> DeviceType.BUZZ
            "fan" -> DeviceType.FAN
            "servo" -> DeviceType.SERVO
            "window" -> DeviceType.WINDOW
            "door" -> DeviceType.DOOR
            "display" -> DeviceType.DISPLAY
            "photo" -> DeviceType.PHOTO
            "brightness" -> DeviceType.BRIGHTNESS
            "motion" -> DeviceType.MOTION
            "temperature" -> DeviceType.TEMPERATURE
            "tilt" -> DeviceType.TILT
            else -> DeviceType.UNKNOWN // Fallback
        }

    // Clean display name: ignore null, blank, or "null" strings
    val displayName: String
        get() = name?.takeIf { it.isNotBlank() && it.lowercase() != "null" } ?: "Unknown Device"

    // Clean display room: null if no room assigned
    val displayRoom: String?
        get() = room?.takeIf { it.isNotBlank() && it.lowercase() != "null" }

    companion object {
        fun fromBackendJson(json: JSONObject): Device {
            return Device(
                id = json.optString("id"),
                room = json.optString("room", null),
                type = json.optString("type"),
                online = json.optBoolean("online", false),
                ip = json.optString("ip", null),
                name = json.optString("name", null),
                description = json.optString("description", null),
                value = json.optInt("value", 0),
                maxValue = json.optInt("max_value", 1),
                minValue = json.optInt("min_value", 0),
                scaleName = json.optString("scale_name", null),

                users = json.optJSONArray("users")?.let { array ->
                    List(array.length()) { i ->
                        val obj = array.getJSONObject(i)
                        User.fromBackendJson(
                            id = obj.optString("id"),
                            username = obj.optString("username"),
                            type = obj.optString("type")
                        )
                    }
                } ?: emptyList()
            )
        }
    }
}
