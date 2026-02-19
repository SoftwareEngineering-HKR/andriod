package se.hkr.andriod.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: UserRole,
)
