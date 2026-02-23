package se.hkr.andriod.ui.devices.lock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@Composable
fun LockDeviceRenderer(
    viewModel: LockViewModel
) {
    val state by viewModel.lockState.collectAsState()

    // Lock control
    LockStateComponent(
        isLocked = state.isLocked,
        onToggle = viewModel::toggleLock
    )

    AutoLockComponent(
        autoLockSeconds = state.autoLockSeconds,
        isExpanded = state.isAutoLockExpanded,
        onToggleExpand = viewModel::toggleAutoLockDropdown,
        onSelect = viewModel::setAutoLock
    )
}