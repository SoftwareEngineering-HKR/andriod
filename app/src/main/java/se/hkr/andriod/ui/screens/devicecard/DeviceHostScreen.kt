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

@Composable
fun DeviceHostScreen(
    device: Device,
    onBackClick: () -> Unit
) {

    val deviceCardViewModel: DeviceCardViewModel = viewModel()

    DeviceCardScreen(
        device = device,
        viewModel = deviceCardViewModel,
        onBackClick = onBackClick
    ) {

        when (device.deviceTypeEnum) {

            DeviceType.LIGHT -> {
                val lightViewModel = remember(deviceCardViewModel) {
                    LightViewModel(deviceCardViewModel, device)
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