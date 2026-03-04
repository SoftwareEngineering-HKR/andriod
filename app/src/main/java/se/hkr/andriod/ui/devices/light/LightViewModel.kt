package se.hkr.andriod.ui.devices.light

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import se.hkr.andriod.ui.screens.devicecard.DeviceCardViewModel
import se.hkr.andriod.ui.screens.devicecard.DeviceScreenUiState

class LightViewModel(
    private val deviceCardViewModel: DeviceCardViewModel
) : ViewModel() {

    // Light specific UI states
    data class LightUiState(
        val brightness: Float = 0.5f,
        val hue: Float = 45f
    )

    private val _lightState = MutableStateFlow(LightUiState())
    val lightState: StateFlow<LightUiState> = _lightState

    init {
        // Adds template level data (name, icon, room, etc.)
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