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
}
