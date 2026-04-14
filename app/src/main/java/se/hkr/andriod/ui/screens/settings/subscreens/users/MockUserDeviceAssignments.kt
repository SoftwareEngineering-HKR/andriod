package se.hkr.andriod.ui.screens.settings.subscreens.users

import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.data.mock.MockUsers

object MockUserDeviceAssignments {

    val assignments: Map<String, Set<String>> = mapOf(
        MockUsers.adminUser.id.toString() to setOf(
            MockDevices.light1.id,
            MockDevices.light2.id,
            MockDevices.doorLock.id,
            MockDevices.tempSensor.id
        ),
        MockUsers.baseUser.id.toString() to setOf(
            MockDevices.light3.id,
            MockDevices.motionSensor.id
        ),
        MockUsers.baseWithRename.id.toString() to setOf(
            MockDevices.light4.id
        )
    )
}