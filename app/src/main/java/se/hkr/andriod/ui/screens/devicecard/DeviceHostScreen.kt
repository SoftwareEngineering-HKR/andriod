package se.hkr.andriod.ui.screens.devicecard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import se.hkr.andriod.domain.model.device.Device
import se.hkr.andriod.domain.model.device.DeviceType
import se.hkr.andriod.ui.devices.light.LightDeviceRenderer
import se.hkr.andriod.ui.devices.light.LightViewModel
import se.hkr.andriod.ui.devices.lock.LockDeviceRenderer
import se.hkr.andriod.ui.devices.lock.LockViewModel
import se.hkr.andriod.data.network.ConnectionManager
import se.hkr.andriod.ui.devices.display.DisplayDeviceRenderer
import se.hkr.andriod.ui.devices.display.DisplayViewModel

class DeviceViewModelFactory(
    private val deviceCardViewModel: DeviceCardViewModel,
    private val device: Device,
    private val connectionManager: ConnectionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LightViewModel::class.java) -> 
                LightViewModel(deviceCardViewModel, device, connectionManager) as T
            modelClass.isAssignableFrom(LockViewModel::class.java) -> 
                LockViewModel(deviceCardViewModel, device) as T
            modelClass.isAssignableFrom(DisplayViewModel::class.java) -> 
                DisplayViewModel(deviceCardViewModel, device, connectionManager) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@Composable
fun DeviceHostScreen(
    device: Device,
    connectionManager: ConnectionManager,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val deviceCardViewModel: DeviceCardViewModel = viewModel()

    DeviceCardScreen(
        device = device,
        viewModel = deviceCardViewModel,
        connectionManager = connectionManager,
        navController = navController,
        onBackClick = onBackClick
    ) { liveDevice ->
        val factory = remember(device.id) {
            DeviceViewModelFactory(deviceCardViewModel, liveDevice, connectionManager)
        }

        when (device.deviceTypeEnum) {
            DeviceType.LIGHT -> {
                val lightViewModel: LightViewModel = viewModel(
                    key = "light_${device.id}",
                    factory = factory
                )
                LightDeviceRenderer(lightViewModel, liveDevice)
            }

            DeviceType.LOCK -> {
                val lockViewModel: LockViewModel = viewModel(
                    key = "lock_${device.id}",
                    factory = factory
                )
                LockDeviceRenderer(lockViewModel)
            }

            DeviceType.DISPLAY -> {
                val displayViewModel: DisplayViewModel = viewModel(
                    key = "display_${device.id}",
                    factory = factory
                )
                DisplayDeviceRenderer(displayViewModel, liveDevice)
            }

            else -> {}
        }
    }
}
