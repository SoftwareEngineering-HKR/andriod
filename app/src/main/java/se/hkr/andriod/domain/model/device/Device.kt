package se.hkr.andriod.domain.model.device

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
        get() = when (type.lowercase()) {
            "light" -> DeviceType.LIGHT
            "lock" -> DeviceType.LOCK
            "sensor" -> DeviceType.SENSOR
            else -> DeviceType.SENSOR // fallback
        }
}
