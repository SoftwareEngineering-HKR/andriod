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
    val showDeleteUserDialog: Boolean = false
)

class UsersViewModel(
    private val userStore: UserStore,
    private val deviceStore: DeviceStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    init {
        userStore.fetchUsers()
        deviceStore.fetchAllDeviceInfo()
        observeStores()
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
                    // preserve dialog state across refresh
                    showDeleteUserDialog = _uiState.value.showDeleteUserDialog
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onUserSelected(userId: UUID) {
        _uiState.update { it.copy(selectedUserId = userId) }
    }

    fun onDeviceToggled(userId: UUID, deviceId: String) {
        viewModelScope.launch {
            userStore.addUserToDevice(userId, deviceId)

            // Not a good solution but it works for now
            deviceStore.fetchAllDeviceInfo()
            delay(100)
            deviceStore.fetchAllDeviceInfo()
        }
    }

    fun onUserRoleChanged(userId: UUID, role: UserRole) {
        viewModelScope.launch {
            // TODO: replace with real backend call
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

    fun deleteSelectedUser() {
        val userId = _uiState.value.selectedUserId ?: return

        viewModelScope.launch {
            // TODO: replace with real backend call
            dismissDialogs()
        }
    }
}
