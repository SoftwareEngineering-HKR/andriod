package se.hkr.andriod.ui.devices.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel

class DisplayViewModel(
    private val deviceCardViewModel: DeviceCardViewModel,
    private val device: Device,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    data class DisplayUiState(
        val text: String = ""
    )

    private val _state = MutableStateFlow(DisplayUiState())
    val state: StateFlow<DisplayUiState> = _state

    init {
        _state.value = DisplayUiState(
            text = device.value.toString()
        )

        // Listen for backend updates
        viewModelScope.launch {
            connectionManager.deviceStore.devices.collect { devices ->
                val updated = devices.firstOrNull { it.id == device.id } ?: return@collect

                _state.update {
                    it.copy(text = updated.value.toString())
                }
            }
        }
    }

    fun setText(newText: String) {
        val trimmed = newText.take(32)
        _state.update { it.copy(text = trimmed) }
    }

    fun commitText() {
        connectionManager.deviceStore.updateDeviceValue(
            device.id,
            _state.value.text
        )
    }
}
