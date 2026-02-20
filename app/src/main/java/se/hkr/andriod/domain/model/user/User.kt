package se.hkr.andriod.domain.model.user

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val role: UserRole,
    val extraPermissions: Set<Permission> = emptySet()
) {

    fun hasPermission(permission: Permission): Boolean {
        return effectivePermissions().contains(permission)
    }

    fun effectivePermissions(): Set<Permission> {
        return role.defaultPermissions + extraPermissions
    }

    // Permission helpers
    fun canAddDevice(): Boolean = effectivePermissions().contains(Permission.ADD_DEVICE)

    fun canAddRoom(): Boolean = effectivePermissions().contains(Permission.ADD_ROOM)

    // Helper to check if the plus button on the overview page should be visible at all.
    fun canShowPlus(): Boolean = canAddDevice() || canAddRoom()
}
