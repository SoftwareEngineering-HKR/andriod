package se.hkr.andriod.data.mock

import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.Room
import java.util.UUID

object MockDevices {

    // Rooms
    val livingRoom = Room(
        id = UUID.randomUUID().toString(),
        name = "Living Room"
    )
    val kitchen = Room(
        id = UUID.randomUUID().toString(),
        name = "Kitchen"
    )

    // Devices
    val light1 = Device(
        id = "light1",
        room = livingRoom.id.toString(),
        type = "light",
        online = true,
        ip = "192.168.0.10",
        name = "Ceiling Light",
        description = "Main living room light",
        value = 1,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val light2 = Device(
        id = "light2",
        room = livingRoom.id.toString(),
        type = "light",
        online = false,
        ip = "192.168.0.11",
        name = "Light 2",
        description = "Secondary living room light",
        value = 0,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val light3 = Device(
        id = "light3",
        room = livingRoom.id.toString(),
        type = "light",
        online = false,
        ip = "192.168.0.12",
        name = "Light 3",
        description = "Corner light",
        value = 0,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val light4 = Device(
        id = "light4",
        room = livingRoom.id.toString(),
        type = "light",
        online = false,
        ip = "192.168.0.13",
        name = "Light 4",
        description = "Reading light",
        value = 0,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val doorLock = Device(
        id = "doorLock1",
        room = livingRoom.id.toString(),
        type = "lock",
        online = true,
        ip = "192.168.0.20",
        name = "Front Door Lock",
        description = "Smart lock on the front door",
        value = 0,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val tempSensor = Device(
        id = "tempSensor1",
        room = livingRoom.id.toString(),
        type = "sensor",
        online = true,
        ip = "192.168.0.30",
        name = "Temperature Sensor",
        description = "Living room temperature sensor",
        value = 21,
        minValue = -50,
        maxValue = 50,
        scaleName = "°C"
    )

    val motionSensor = Device(
        id = "motionSensor1",
        room = kitchen.id.toString(),
        type = "sensor",
        online = true,
        ip = "192.168.0.31",
        name = "Motion Sensor",
        description = "Detects motion in the kitchen",
        value = 0,
        minValue = 0,
        maxValue = 1,
        scaleName = null
    )

    val allDevices = listOf(light1, light2, light3, light4, doorLock, tempSensor, motionSensor)
}
