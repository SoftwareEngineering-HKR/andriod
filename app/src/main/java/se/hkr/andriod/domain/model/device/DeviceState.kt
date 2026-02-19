package se.hkr.andriod.domain.model.device

data class DeviceState(
    val isOn: Boolean? = null, // for lights or locks etc.
    val numericValue: Float? = null, // for sensors like temperature, humidity etc.
    val status: String? = null, // generic status for any device
)
