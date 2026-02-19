package se.hkr.andriod.data.mock

import se.hkr.andriod.domain.model.device.*
import java.util.UUID

object MockDevices {

    // Rooms
    val livingRoom = Room(
        id = UUID.randomUUID(),
        name = "Living Room"
    )
    val kitchen = Room(
        id = UUID.randomUUID(),
        name = "Kitchen"
    )

    // Devices
    val light1 = Device(
        id = UUID.randomUUID(),
        name = "Ceiling Light",
        description = "Main living room light",
        roomId = livingRoom.id,
        type = DeviceType.LIGHT,
        state = DeviceState(isOn = true)
    )

    val doorLock = Device(
        id = UUID.randomUUID(),
        name = "Front Door Lock",
        description = "Smart lock on the front door",
        roomId = livingRoom.id,
        type = DeviceType.LOCK,
        state = DeviceState(isOn = false)
    )

    val tempSensor = Device(
        id = UUID.randomUUID(),
        name = "Temperature Sensor",
        description = "Living room temperature sensor",
        roomId = livingRoom.id,
        type = DeviceType.SENSOR,
        sensorType = SensorType.TEMPERATURE,
        state = DeviceState(numericValue = 21.5f)
    )

    val motionSensor = Device(
        id = UUID.randomUUID(),
        name = "Motion Sensor",
        description = "Detects motion in the kitchen",
        roomId = kitchen.id,
        type = DeviceType.SENSOR,
        sensorType = SensorType.MOTION,
        state = DeviceState(status = "No Motion")
    )

    val allDevices = listOf(light1, doorLock, tempSensor, motionSensor)
}
