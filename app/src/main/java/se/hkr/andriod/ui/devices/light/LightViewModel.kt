package se.hkr.andriod.ui.devices.light

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel

class LightViewModel(
    private val deviceCardViewModel: DeviceCardViewModel,
    device: Device
) : ViewModel() {

    // Light specific UI states
    data class LightUiState(
        val brightness: Float = 0.5f,
        val hue: Float = 45f
    )

    private val _lightState = MutableStateFlow(LightUiState())
    val lightState: StateFlow<LightUiState> = _lightState

    init {
        // Connect the actual device to the DeviceCardViewModel
        deviceCardViewModel.setDevice(device)
    }

    // Updates brightness state
    fun setBrightness(value: Float) {
        _lightState.update { it.copy(brightness = value) }
    }

    // Updates color state
    fun setHue(newHue: Float) {
        _lightState.update { it.copy(hue = newHue) }
    }
}