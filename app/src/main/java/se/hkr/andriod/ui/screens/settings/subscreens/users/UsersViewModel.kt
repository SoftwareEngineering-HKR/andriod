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
    val selectedUserId: UUID? = null,
    val editedRoles: Map<UUID, UserRole> = emptyMap(),
    val editedPermissions: Map<UUID, Set<Permission>> = emptyMap()
)

class UsersViewModel : ViewModel() {
    private val initialUsers = MockUsers.allUsers

    private val _uiState = MutableStateFlow(
        UsersUiState(
            users = initialUsers,
            selectedUserId = initialUsers.firstOrNull()?.id
        )
    )

    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun onUserSelected(userId: UUID) {
        _uiState.update { state ->
            state.copy(selectedUserId = userId)
        }
    }
    fun onRoleChanged(role: UserRole) {
        val selectedUser = getSelectedUser() ?: return

        _uiState.update { state ->
            state.copy(
                editedRoles = state.editedRoles + (selectedUser.id to role)
            )
        }
    }

    fun onPermissionToggled(permission: Permission) {
        val selectedUser = getSelectedUser() ?: return

        _uiState.update { state ->
            val currentPermissions = state.editedPermissions[selectedUser.id]
                ?: selectedUser.extraPermissions

            val updatedPermissions =
                if (permission in currentPermissions) {
                    currentPermissions - permission
                } else {
                    currentPermissions + permission
                }

            state.copy(
                editedPermissions = state.editedPermissions + (selectedUser.id to updatedPermissions)
            )
        }
    }

    fun saveSelectedUser() {
        val selectedUser = getSelectedUser() ?: return

        _uiState.update { state ->
            val updatedUser = selectedUser.copy(
                role = getEditedRole(state, selectedUser),
                extraPermissions = getEditedPermissions(state, selectedUser)
            )

            state.copy(
                users = state.users.map { user ->
                    if (user.id == selectedUser.id) updatedUser else user
                },
                editedRoles = state.editedRoles - selectedUser.id,
                editedPermissions = state.editedPermissions - selectedUser.id
            )
        }
    }

    fun getSelectedUser(): User? {
        val state = _uiState.value
        return state.users.find { it.id == state.selectedUserId }
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
        return state.editedRoles[user.id] ?: user.role
    }

    private fun getEditedPermissions(state: UsersUiState, user: User): Set<Permission> {
        return state.editedPermissions[user.id] ?: user.extraPermissions
    }


}