package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.UserStore
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

data class UsersUiState(
    val users: List<User> = emptyList(),
    val devices: List<Device> = emptyList(),
    val selectedUserId: UUID? = null,
    val isLoaded: Boolean = false,
    val showDeleteUserDialog: Boolean = false
)

class UsersViewModel(
    private val userStore: UserStore,
    private val deviceStore: DeviceStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    private var minimumLoadingFinished = false

    init {
        userStore.fetchUsers()
        deviceStore.fetchAllDeviceInfo()

        startMinimumLoadingTimer()
        observeStores()
    }

    private fun startMinimumLoadingTimer() {
        viewModelScope.launch {
            delay(500)
            minimumLoadingFinished = true

            // Re-check after delay finishes
            checkIfLoaded()
        }
    }

    private fun checkIfLoaded() {
        val hasUsers = _uiState.value.users.isNotEmpty()

        if (hasUsers && minimumLoadingFinished) {
            _uiState.update {
                it.copy(isLoaded = true)
            }
        }
    }

    private fun observeStores() {
        viewModelScope.launch {
            combine(
                userStore.users,
                deviceStore.allDevices,
                _uiState.map { it.selectedUserId }
            ) { users, devices, selectedUserId ->

                UsersUiState(
                    users = users,
                    devices = devices,
                    selectedUserId = selectedUserId ?: users.firstOrNull()?.id,
                    isLoaded = _uiState.value.isLoaded,
                    showDeleteUserDialog = _uiState.value.showDeleteUserDialog
                )
            }.collect { state ->
                _uiState.value = state
                checkIfLoaded()
            }
        }
    }

    fun onUserSelected(userId: UUID) {
        _uiState.update { it.copy(selectedUserId = userId) }
    }

    fun onDeviceToggled(userId: UUID, deviceId: String) {
        viewModelScope.launch {
            val currentDevice = _uiState.value.devices.find { it.id == deviceId }

            val userAlreadyHasDevice = currentDevice?.users?.any { it.id == userId } == true

            if (userAlreadyHasDevice) {
                userStore.removeUserFromDevice(userId, deviceId)
            } else {
                userStore.addUserToDevice(userId, deviceId)
            }
        }
    }

    fun onUserRoleChanged(userName: String, role: UserRole) {
        val backendRole = role.toBackendType()
        viewModelScope.launch {
            userStore.updateUserRole(userName, backendRole)
        }
    }

    fun showDeleteUserDialog() {
        _uiState.update {
            it.copy(showDeleteUserDialog = true)
        }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(showDeleteUserDialog = false)
        }
    }

    fun deleteSelectedUser(userName: String) {
        viewModelScope.launch {
            userStore.deleteUser(userName)
            dismissDialogs()
        }
    }
}
