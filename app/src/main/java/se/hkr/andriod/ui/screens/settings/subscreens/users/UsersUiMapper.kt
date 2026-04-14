package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.annotation.StringRes
import se.hkr.andriod.R
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

data class UserInfoUi(
    val name: String,
    @StringRes val roleRes: Int,
    val assignedDeviceCount: Int
)

fun getSelectedUser(
    users: List<User>,
    selectedUserId: UUID?
): User? {
    return users.find { it.id == selectedUserId }
}

fun getDisplayedAssignedDeviceIds(
    user: User,
    editedAssignments: Map<UUID, Set<String>>,
    savedAssignments: Map<UUID, Set<String>>
): Set<String> {
    return editedAssignments[user.id]
        ?: savedAssignments[user.id]
        ?: emptySet()
}

fun mapUserToInfoUi(
    user: User?,
    displayedAssignedDeviceIds: Set<String>
): UserInfoUi {
    if (user == null) {
        return UserInfoUi(
            name = "",
            roleRes = R.string.base_user,
            assignedDeviceCount = 0
        )
    }

    return UserInfoUi(
        name = user.username,
        roleRes = user.role.toRoleTextRes(),
        assignedDeviceCount = displayedAssignedDeviceIds.size
    )
}

@StringRes
fun UserRole.toRoleTextRes(): Int {
    return when (this) {
        UserRole.ADMIN -> R.string.admin
        UserRole.BASE -> R.string.base_user
    }
}

@StringRes
fun deviceTypeToTextRes(type: String): Int {
    return when (type.lowercase()) {
        "light" -> R.string.device_type_light
        "lock" -> R.string.device_type_lock
        "sensor" -> R.string.device_type_sensor
        else -> R.string.device_type_unknown
    }
}

@StringRes
fun deviceStatusToTextRes(isOnline: Boolean): Int {
    return if (isOnline) {
        R.string.online
    } else {
        R.string.offline
    }
}

fun Device.displayName(): String {
    return if (name.isNullOrBlank() || name == "null") {
        id
    } else {
        name.orEmpty()
    }
}

fun Device.displayDescription(): String {
    return if (description.isNullOrBlank() || description == "null") {
        ""
    } else {
        description.orEmpty()
    }
}

fun resolveRoomName(roomId: String?): String {
    if (roomId.isNullOrBlank()) return ""

    return listOf(
        MockDevices.livingRoom,
        MockDevices.kitchen
    ).firstOrNull { it.id == roomId }?.name.orEmpty()
}