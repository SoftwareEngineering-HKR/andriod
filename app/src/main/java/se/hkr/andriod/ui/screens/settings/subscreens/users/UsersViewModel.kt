package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.mock.MockUsers
import se.hkr.andriod.domain.model.user.Permission
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

data class UsersUiState(
    val users: List<User> = emptyList(),
    val editedRoles: Map<UUID, UserRole> = emptyMap(),
    val editedPermissions: Map<UUID, Set<Permission>> = emptyMap()
)

class UsersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        UsersUiState(
            users = MockUsers.allUsers
        )
    )

    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun onRoleChanged(userId: UUID, role: UserRole) {
        _uiState.update { state ->
            state.copy(
                editedRoles = state.editedRoles + (userId to role)
            )
        }
    }

    fun onPermissionToggled(userId: UUID, permission: Permission) {
        _uiState.update { state ->
            val user = state.users.find { it.id == userId } ?: return@update state

            val currentPermissions = state.editedPermissions[userId]
                ?: user.extraPermissions

            val updatedPermission =
                if (permission in currentPermissions) {
                    currentPermissions - permission
                } else {
                    currentPermissions + permission
                }

            state.copy(
                editedPermissions = state.editedPermissions + (userId to updatedPermission)
            )

        }
    }

    fun saveUser(userId: UUID) {
        _uiState.update { state ->
            val user = state.users.find { it.id == userId } ?: return@update state

            val updatedUser = user.copy(
                role = getEditedRole(state, user),
                extraPermissions = getEditedPermissions(state, user)
            )

            state.copy(
                users = state.users.map {
                    if (it.id == userId) updatedUser else it
                },
                editedRoles = state.editedRoles - userId,
                editedPermissions = state.editedPermissions - userId
            )
        }
    }

    fun getDisplayedRole(user: User): UserRole {
        return getEditedRole(_uiState.value, user)
    }

    fun getDisplayedPermissions(user: User): Set<Permission> {
        return getEditedPermissions(_uiState.value, user)
    }

    fun isSaveEnabled(user: User): Boolean {
        val state = _uiState.value
        return getEditedRole(state, user) != user.role ||
                getEditedPermissions(state, user) != user.extraPermissions
    }

    private fun getEditedRole(state: UsersUiState, user: User): UserRole {
        return state.editedRoles[user.id] ?: user.role]
    }

    private fun getEditedPermissions(state: UsersUiState, user: User): Set<Permission> {
        return state.editedPermissions[user.id] ?: user.extraPermissions
    }


}