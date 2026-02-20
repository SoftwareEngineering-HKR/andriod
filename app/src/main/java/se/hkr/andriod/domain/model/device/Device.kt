package se.hkr.andriod.domain.model.device

import java.util.UUID

data class Device(
    val id: UUID,
    val name: String,
    val description: String,
    val roomId: UUID,
    val type: DeviceType,
    val sensorType: SensorType? = null,
    val state: DeviceState
)
