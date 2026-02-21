package se.hkr.andriod.ui.devices.light

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun LightDeviceRenderer(
    viewModel: LightViewModel
) {
    val state by viewModel.lightState.collectAsState()

    val components = viewModel.buildComponents()


    components.forEach { component ->
        when (component) {
            is LightComponent.Brightness -> {
                BrightnessComponent(
                    value = component.value,
                    onValueChange = viewModel::setBrightness
                )
            }

            is LightComponent.ColorPicker -> {
                ColorPickerComponent(
                    selectedColor = component.color,
                    onColorSelected = viewModel::setColor
                )
            }
        }
    }

}