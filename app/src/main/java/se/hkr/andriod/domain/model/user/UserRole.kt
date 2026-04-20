package se.hkr.andriod.domain.model.user

enum class UserRole(
    val defaultPermissions: Set<Permission>
) {

    BASE(
        setOf(
            // minimal permissions for normal users
        )
    ),

    ADMIN(
        Permission.entries.toSet() // all permissions
    );

    companion object {
        fun fromBackendType(type: String): UserRole {
            return when (type.lowercase()) {
                "admin" -> ADMIN
                else -> BASE
            }
        }
    }

    fun toBackendType(): String {
        return when (this) {
            ADMIN -> "admin"
            BASE -> "user"
        }
    }
}
