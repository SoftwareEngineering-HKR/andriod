package se.hkr.andriod.ui.devices.light

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LightDeviceRenderer(
    viewModel: LightViewModel
) {
    val state by viewModel.lightState.collectAsState()

    BrightnessComponent(
        value = state.brightness,
        onValueChange = viewModel::setBrightness
    )

    ColorPickerComponent(
        selectedColor = state.color,
        onColorSelected = viewModel::setColor
    )

}