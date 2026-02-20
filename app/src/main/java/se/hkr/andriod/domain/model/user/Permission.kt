package se.hkr.andriod.domain.model.user

enum class Permission {

    // Room management
    ADD_ROOM,
    REMOVE_ROOM,
    RENAME_ROOM,

    // Device structure
    ADD_DEVICE,
    REMOVE_DEVICE,
    RENAME_DEVICE,
    MOVE_DEVICE,

    // Scheduling
    MANAGE_SCHEDULES,

    // User management
    MANAGE_USERS,
}
