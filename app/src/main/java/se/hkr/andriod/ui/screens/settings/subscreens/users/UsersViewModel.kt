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
            val displayedRole = state.editedRoles[userId] ?: user.role

            if (permission in displayedRole.defaultPermissions) {
                return@update state
            }

            val currentExtraPermissions = state.editedPermissions[userId] ?: user.extraPermissions

            val updatedPermissions =
                if (permission in currentExtraPermissions) {
                    currentExtraPermissions - permission
                } else {
                    currentExtraPermissions + permission
                }

            state.copy(
                editedPermissions = state.editedPermissions + (userId to updatedPermissions)
            )
        }
    }

    fun saveSelectedUser() {
        _uiState.update { state ->
            val selectedUserId = state.selectedUserId ?: return@update state
            val user = state.users.find { it.id == selectedUserId } ?: return@update state

            val updatedUser = user.copy(
                role = state.editedRoles[selectedUserId] ?: user.role,
                extraPermissions = state.editedPermissions[selectedUserId] ?: user.extraPermissions
            )

            state.copy(
                users = state.users.map { currentUser ->
                    if (currentUser.id == selectedUserId) updatedUser else currentUser
                },
                editedRoles = state.editedRoles - selectedUserId,
                editedPermissions = state.editedPermissions - selectedUserId
            )
        }
    }
}