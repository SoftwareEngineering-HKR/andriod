package se.hkr.andriod.ui.devices.light

import se.hkr.andriod.ui.screens.devicecard.DeviceScreenUiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel

class LightViewModel(
    private val deviceCardViewModel: DeviceCardViewModel
) : ViewModel() {

    private val _lightState = MutableStateFlow(LightUiState())
    val lightState: StateFlow<LightUiState> = _lightState

    init {
        deviceCardViewModel.setTemplateState(
            DeviceScreenUiState(
                deviceName = "Living Room Light",
                roomName = "Living Room",
                isOnline = true,
                icon = Icons.Rounded.Lightbulb,
                scheduleExpanded = false,
                lastUpdatedText = "Last updated 1 minute ago"
            )
        )

        buildComponents()
    }

    fun setBrightness(value: Float) {
        _lightState.update { it.copy(brightness = value) }
    }

    fun setColor(color: Color) {
        _lightState.update { it.copy(color = color) }
    }

    fun buildComponents(): List<LightComponent> {
        val state = _lightState.value
        return listOf(
            LightComponent.Brightness(state.brightness),
            LightComponent.ColorPicker(state.color)
        )
    }
}