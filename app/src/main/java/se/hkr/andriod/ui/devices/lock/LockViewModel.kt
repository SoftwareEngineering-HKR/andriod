package se.hkr.andriod.ui.devices.lock

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel
import se.hkr.andriod.ui.screens.devicecard.DeviceScreenUiState
import androidx.lifecycle.ViewModel

class LockViewModel(
    private val deviceCardViewModel: DeviceCardViewModel
) : ViewModel() {

    // Lock specific UI state
    data class LockUiState(
        val isLocked: Boolean = true,
        val batteryPercent: Int = 87,
        val autoLockSeconds: Int? = 60, // null = disabled
        val isAutoLockExpanded: Boolean = false
    )

    private val _lockState = MutableStateFlow(LockUiState())
    val lockState: StateFlow<LockUiState> = _lockState

    init {
        deviceCardViewModel.setTemplateState(
            DeviceScreenUiState(
                deviceName = "Front Door Lock",
                roomName = "Entrance",
                isOnline = true,
                icon = Icons.Rounded.Lock,
                scheduleExpanded = false,
                lastUpdatedText = "Last updated 1 minute ago"
            )
        )
    }

    // Toggle locked / unlocked
    fun toggleLock() {
        _lockState.value = _lockState.value.copy(isLocked = !_lockState.value.isLocked)
    }

    // Auto lock dropdown expand
    fun toggleAutoLockDropdown() {
        _lockState.value = _lockState.value.copy(isAutoLockExpanded = !_lockState.value.isAutoLockExpanded)
    }

    // Batter update (later)
    fun updateBattery(percent: Int) {
        _lockState.update { it.copy(batteryPercent = percent) }
    }

}