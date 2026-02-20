package se.hkr.andriod.data.mock

import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

object MockUsers {
    val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        email = "admin@example.com",
        role = UserRole.ADMIN
    )

    val baseUser = User(
        id = UUID.randomUUID(),
        username = "john_doe",
        email = "john@example.com",
        role = UserRole.BASE
    )

    val baseWithRename = User(
        id = UUID.randomUUID(),
        username = "renamer",
        email = "renamer@example.com",
        role = UserRole.BASE,
        extraPermissions = setOf(Permission.RENAME_DEVICE, Permission.RENAME_ROOM)
    )


    val deviceManager = User(
        id = UUID.randomUUID(),
        username = "manager",
        email = "manager@example.com",
        role = UserRole.DEVICE_MANAGER,
        extraPermissions = setOf(Permission.ADD_DEVICE)
    )

    val allUsers = listOf(adminUser, baseUser)
}
