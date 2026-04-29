package se.hkr.andriod.ui.devices.display

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import se.hkr.andriod.domain.model.device.Device

@Composable
fun DisplayDeviceRenderer(
    viewModel: DisplayViewModel,
    device: Device
) {
    val state by viewModel.state.collectAsState()

    DisplayValueComponent(
        value = state.text
    )

    TextInputComponent(
        value = state.text,
        onValueChange = viewModel::setText,
        onSend = { viewModel.commitText() },
        device = device
    )
}
