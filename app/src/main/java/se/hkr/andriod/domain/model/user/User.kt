package se.hkr.andriod.domain.model.user

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val role: UserRole,
    val extraPermissions: Set<Permission> = emptySet()
) {

    fun hasPermission(permission: Permission): Boolean = effectivePermissions().contains(permission)

    fun effectivePermissions(): Set<Permission> = role.defaultPermissions + extraPermissions

    // Permission helpers
    // Room permissions
    fun canAddRoom(): Boolean = hasPermission(Permission.ADD_ROOM)
    fun canRemoveRoom(): Boolean = hasPermission(Permission.REMOVE_ROOM)
    fun canRenameRoom(): Boolean = hasPermission(Permission.RENAME_ROOM)

    // Device permissions
    fun canAddDevice(): Boolean = hasPermission(Permission.ADD_DEVICE)
    fun canRemoveDevice(): Boolean = hasPermission(Permission.REMOVE_DEVICE)
    fun canRenameDevice(): Boolean = hasPermission(Permission.RENAME_DEVICE)
    fun canMoveDevice(): Boolean = hasPermission(Permission.MOVE_DEVICE)

    // Scheduling permissions
    fun canManageSchedules(): Boolean = hasPermission(Permission.MANAGE_SCHEDULES)

    // User management permissions
    fun canManageUsers(): Boolean = hasPermission(Permission.MANAGE_USERS)


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
                    canRenameDevice() || canMoveDevice()
            )

    fun canViewRooms(): Boolean = (
            canAddRoom() || canRemoveRoom() ||
                    canRenameRoom() || canMoveDevice()
            )

    companion object {
        // Factory to create User from backend JSON
        fun fromBackendJson(
            id: String,
            username: String,
            type: String,
            extraPermissions: Set<Permission> = emptySet()
        ): User {
            return User(
                id = UUID.fromString(id),
                username = username,
                role = UserRole.fromBackendType(type),
                extraPermissions = extraPermissions
            )
        }
    }
}
