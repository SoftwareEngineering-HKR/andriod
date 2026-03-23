package se.hkr.andriod.domain.model.device

import org.json.JSONObject

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
    var scaleName: String? = null
) {
    // Computed property for UI
    val deviceTypeEnum: DeviceType
        get() = when(type.lowercase()) {
            "light" -> DeviceType.LIGHT
            "lock" -> DeviceType.LOCK
            "sensor" -> DeviceType.SENSOR
            else -> DeviceType.SENSOR // fallback
        }

    val displayName: String
        get() = name ?: "Unknown Device"

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
                scaleName = json.optString("scale_name", null)
            )
        }
    }
}
