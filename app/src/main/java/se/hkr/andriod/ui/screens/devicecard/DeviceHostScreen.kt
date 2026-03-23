package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.ui.devices.light.LightDeviceRenderer
import se.hkr.andriod.ui.devices.light.LightViewModel
import se.hkr.andriod.ui.devices.lock.LockDeviceRenderer
import se.hkr.andriod.ui.devices.lock.LockViewModel
import se.hkr.andriod.data.network.ConnectionManager

@Composable
fun DeviceHostScreen(
    device: Device,
    connectionManager: ConnectionManager,
    onBackClick: () -> Unit
) {

    val deviceCardViewModel: DeviceCardViewModel = viewModel()

    DeviceCardScreen(
        device = device,
        viewModel = deviceCardViewModel,
        connectionManager = connectionManager,
        onBackClick = onBackClick
    ) {

        when (device.deviceTypeEnum) {

            DeviceType.LIGHT -> {
                val lightViewModel = remember(deviceCardViewModel) {
                    LightViewModel(deviceCardViewModel, device, connectionManager)
                }
                LightDeviceRenderer(lightViewModel)
            }

            DeviceType.LOCK -> {
                val lockViewModel = remember(deviceCardViewModel) {
                    LockViewModel(deviceCardViewModel, device)
                }
                LockDeviceRenderer(lockViewModel)
            }

            else -> {}
        }
    }
}
