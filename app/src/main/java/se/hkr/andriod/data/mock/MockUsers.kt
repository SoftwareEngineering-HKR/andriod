package se.hkr.andriod.data.mock

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

    val normalUser = User(
        id = UUID.randomUUID(),
        username = "john_doe",
        email = "john@example.com",
        role = UserRole.NORMAL
    )

    val allUsers = listOf(adminUser, normalUser)
}
