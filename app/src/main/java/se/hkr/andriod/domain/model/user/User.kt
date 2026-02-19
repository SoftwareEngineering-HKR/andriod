package se.hkr.andriod.domain.model.user

import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val role: UserRole,
)
