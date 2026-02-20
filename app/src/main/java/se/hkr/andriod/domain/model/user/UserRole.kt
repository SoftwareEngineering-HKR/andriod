package se.hkr.andriod.domain.model.user

enum class UserRole(
    val defaultPermissions: Set<Permission>
) {

    BASE(
        setOf(
        )
    ),

    DEVICE_MANAGER(
        setOf(
            Permission.ADD_ROOM,
            Permission.REMOVE_ROOM,
            Permission.RENAME_ROOM,
            Permission.ADD_DEVICE,
            Permission.REMOVE_DEVICE,
            Permission.RENAME_DEVICE,
            Permission.MOVE_DEVICE,
            Permission.MANAGE_SCHEDULES,
        )
    ),

    ADMIN(
        Permission.entries.toSet()
    )
}
