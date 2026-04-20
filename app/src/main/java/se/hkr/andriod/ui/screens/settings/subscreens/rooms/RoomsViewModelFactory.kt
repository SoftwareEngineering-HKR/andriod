package se.hkr.andriod.ui.screens.settings.subscreens.rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.RoomStore

class RoomsViewModelFactory(
    private val roomStore: RoomStore,
    private val deviceStore: DeviceStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomsViewModel(roomStore, deviceStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}