package se.hkr.andriod.ui.screens.settings.subscreens.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.RoomStore

class DevicesViewModelFactory(
    private val deviceStore: DeviceStore,
    private val roomStore: RoomStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DevicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DevicesViewModel(deviceStore, roomStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
