package se.hkr.andriod.data.mock

import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

object MockUsers {
    val adminUser = User(
        id = UUID.randomUUID(),
        username = "admin",
        role = UserRole.ADMIN
    )

    val baseUser = User(
        id = UUID.randomUUID(),
        username = "john_doe",
        role = UserRole.BASE
    )

    val baseWithRename = User(
        id = UUID.randomUUID(),
        username = "renamer",
        role = UserRole.BASE,
        extraPermissions = setOf(Permission.RENAME_DEVICE, Permission.RENAME_ROOM)
    )

    val allUsers = listOf(adminUser, baseUser, baseWithRename)
}
