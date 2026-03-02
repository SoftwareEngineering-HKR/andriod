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
    // Room permissions
    fun canAddRoom(): Boolean = effectivePermissions().contains(Permission.ADD_ROOM)
    fun canRemoveRoom(): Boolean = effectivePermissions().contains(Permission.REMOVE_ROOM)
    fun canRenameRoom(): Boolean = effectivePermissions().contains(Permission.RENAME_ROOM)

    // Device permissions
    fun canAddDevice(): Boolean = effectivePermissions().contains(Permission.ADD_DEVICE)
    fun canRemoveDevice(): Boolean = effectivePermissions().contains(Permission.REMOVE_DEVICE)
    fun canRenameDevice(): Boolean = effectivePermissions().contains(Permission.RENAME_DEVICE)
    fun canMoveDevice(): Boolean = effectivePermissions().contains(Permission.MOVE_DEVICE)

    // Scheduling permissions
    fun canManageSchedules(): Boolean = effectivePermissions().contains(Permission.MANAGE_SCHEDULES)

    // User management permissions
    fun canManageUsers(): Boolean = effectivePermissions().contains(Permission.MANAGE_USERS)


    // Helper to check if the plus button on the overview page should be visible at all.
    fun canShowPlus(): Boolean = canAddDevice() || canAddRoom()

    // Helper to check if the Household Settings header should display.
    fun canShowHouseholdSettings(): Boolean = (
            canManageUsers() || canAddDevice() || canRemoveDevice() ||
                    canRenameDevice() || canMoveDevice() || canAddRoom() ||
                    canRemoveRoom() || canRenameRoom() || canManageSchedules()
    )

    // Helpers to check if current user can view the Device or Rooms pages in settings.
    fun canViewDevices(): Boolean = (
            canAddDevice() || canRemoveDevice() ||
                    canRenameDevice() || canMoveDevice())

    fun canViewRooms(): Boolean = (
            canAddRoom() || canRemoveRoom() ||
                    canRenameRoom() || canMoveDevice())
}
