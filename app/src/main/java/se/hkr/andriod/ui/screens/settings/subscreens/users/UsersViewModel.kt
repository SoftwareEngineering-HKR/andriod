package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.data.mock.MockDevices
import se.hkr.andriod.data.mock.MockUsers
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.user.User
import java.util.UUID

data class UsersUiState(
    val users: List<User> = emptyList(),
    val devices: List<Device> = emptyList(),
    val selectedUserId: UUID? = null,
    val savedAssignments: Map<UUID, Set<String>> = emptyMap(),
    val editedAssignments: Map<UUID, Set<String>> = emptyMap()
)

class UsersViewModel : ViewModel() {

    private val initialUsers = MockUsers.allUsers
    private val initialDevices = MockDevices.allDevices

    private val initialAssignments: Map<UUID, Set<String>> =
        MockUserDeviceAssignments.assignments.mapNotNull { (userIdString, deviceIds) ->
            userIdString.toUUIDOrNull()?.let { uuid ->
                uuid to deviceIds
            }
        }.toMap()

    private val _uiState = MutableStateFlow(
        UsersUiState(
            users = initialUsers,
            devices = initialDevices,
            selectedUserId = initialUsers.firstOrNull()?.id,
            savedAssignments = initialAssignments
        )
    )
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun onUserSelected(userId: UUID) {
        _uiState.update { state ->
            state.copy(selectedUserId = userId)
        }
    }

    fun onDeviceToggled(userId: UUID, deviceId: String) {
        _uiState.update { state ->
            val currentAssignments = state.editedAssignments[userId]
                ?: state.savedAssignments[userId]
                ?: emptySet()

            val updatedAssignments =
                if (deviceId in currentAssignments) {
                    currentAssignments - deviceId
                } else {
                    currentAssignments + deviceId
                }

            state.copy(
                editedAssignments = state.editedAssignments + (userId to updatedAssignments)
            )
        }
    }

    fun saveSelectedUser() {
        _uiState.update { state ->
            val selectedUserId = state.selectedUserId ?: return@update state
            val updatedAssignments = state.editedAssignments[selectedUserId] ?: return@update state

            state.copy(
                savedAssignments = state.savedAssignments + (selectedUserId to updatedAssignments),
                editedAssignments = state.editedAssignments - selectedUserId
            )
        }
    }

    fun hasUnsavedChanges(userId: UUID): Boolean {
        val state = _uiState.value
        val saved = state.savedAssignments[userId] ?: emptySet()
        val edited = state.editedAssignments[userId] ?: saved
        return saved != edited
    }

    private fun String.toUUIDOrNull(): UUID? {
        return try {
            UUID.fromString(this)
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}