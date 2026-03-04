package se.hkr.andriod.ui.devices.light

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LightDeviceRenderer(
    viewModel: LightViewModel
) {
    val state by viewModel.lightState.collectAsState()

    // Brightness control
    BrightnessComponent(
        value = state.brightness,
        onValueChange = viewModel::setBrightness
    )

    // Color control
    ColorPickerComponent(
        hue = state.hue,
        onColorSelected = viewModel::setHue
    )

}