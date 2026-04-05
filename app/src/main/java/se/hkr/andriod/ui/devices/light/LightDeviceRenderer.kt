package se.hkr.andriod.ui.devices.light

import android.R.attr.enabled
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import se.hkr.andriod.domain.model.device.Device

@Composable
fun LightDeviceRenderer(
    viewModel: LightViewModel,
    device: Device
) {
    val state by viewModel.lightState.collectAsState()

    // Brightness control
    BrightnessComponent(
        value = state.brightness,
        onValueChange = viewModel::setBrightness,
        onValueChangeFinished = { viewModel.commitBrightness() },
        device = device
    )

    // Color control
    ColorPickerComponent(
        hue = state.hue,
        onColorSelected = viewModel::setHue,
        device = device
    )
}
