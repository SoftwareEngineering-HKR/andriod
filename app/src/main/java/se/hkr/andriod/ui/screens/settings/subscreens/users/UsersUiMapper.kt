package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.annotation.StringRes
import se.hkr.andriod.R
import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole

data class UserInfoUi(
    val name: String,
    @StringRes val roleRes: Int,
    val activePermissionCount: Int
)

data class PermissionUi(
    val permission: Permission,
    @StringRes val labelRes: Int
)

fun mapUserToInfoUi(
    user: User?,
    selectedRole: UserRole?,
    selectedPermissions: Set<Permission>?
): UserInfoUi {
    if (user == null) {
        return UserInfoUi(
            name = "",
            roleRes = R.string.base_user,
            activePermissionCount = 0
        )
    }

    val displayedRole = selectedRole ?: user.role
    val displayedExtraPermissions = selectedPermissions ?: user.extraPermissions
    val effectivePermissions = displayedRole.defaultPermissions + displayedExtraPermissions

    return UserInfoUi(
        name = user.username,
        roleRes = displayedRole.toRoleTextRes(),
        activePermissionCount = effectivePermissions.size
    )
}

fun mapPermissionsToUi(): List<PermissionUi> {
    return Permission.entries.map { permission ->
        PermissionUi(
            permission = permission,
            labelRes = permission.toPermissionTextRes()
        )
    }
}

@StringRes
fun UserRole.toRoleTextRes(): Int {
    return when (this) {
        UserRole.ADMIN -> R.string.admin
        UserRole.BASE -> R.string.base_user
    }
}

@StringRes
fun Permission.toPermissionTextRes(): Int {
    return when (this) {
        Permission.ADD_ROOM -> R.string.permission_add_room
        Permission.REMOVE_ROOM -> R.string.permission_remove_room
        Permission.RENAME_ROOM -> R.string.permission_rename_room
        Permission.ADD_DEVICE -> R.string.permission_add_device
        Permission.REMOVE_DEVICE -> R.string.permission_remove_device
        Permission.RENAME_DEVICE -> R.string.permission_rename_device
        Permission.MOVE_DEVICE -> R.string.permission_move_device
        Permission.MANAGE_SCHEDULES -> R.string.permission_manage_schedules
        Permission.MANAGE_USERS -> R.string.permission_manage_users
    }
}