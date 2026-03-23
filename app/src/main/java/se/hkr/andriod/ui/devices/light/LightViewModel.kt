package se.hkr.andriod.ui.devices.light

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel
import se.hkr.andriod.data.network.ConnectionManager

class LightViewModel(
    private val deviceCardViewModel: DeviceCardViewModel,
    private val device: Device,
    private val connectionManager: ConnectionManager
) : ViewModel() {

    // Light specific UI states
    data class LightUiState(
        val brightness: Float = 0.5f,
        val hue: Float = 45f
    )

    private val _lightState = MutableStateFlow(LightUiState())
    val lightState: StateFlow<LightUiState> = _lightState

    init {
        // Initialize slider from the device value
        val min = device.minValue
        val max = device.maxValue
        val current = device.value

        val normalized = if (max > min) {
            (current - min).toFloat() / (max - min)
        } else 0f

        _lightState.value = LightUiState(brightness = normalized)

        // Collect live updates from the backend
        viewModelScope.launch {
            connectionManager.deviceStore.devices.collect { devices ->
                val updated = devices.firstOrNull { it.id == device.id } ?: return@collect

                val min = updated.minValue
                val max = updated.maxValue
                val current = updated.value

                val normalized = if (max > min) {
                    (current - min).toFloat() / (max - min)
                } else 0f

                _lightState.update { it.copy(brightness = normalized) }
            }
        }
    }

    // Updates brightness state
    fun setBrightness(value: Float) {
        _lightState.update { it.copy(brightness = value) }
    }

    fun commitBrightness() {
        val brightness = _lightState.value.brightness
        val min = device.minValue
        val max = device.maxValue
        val actualValue = (min + (brightness * (max - min))).toInt()

        connectionManager.deviceStore.updateDeviceValue(device.id, actualValue)
    }

    // Updates color state
    fun setHue(newHue: Float) {
        _lightState.update { it.copy(hue = newHue) }
    }
}