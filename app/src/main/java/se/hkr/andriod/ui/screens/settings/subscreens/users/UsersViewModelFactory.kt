package se.hkr.andriod.ui.screens.settings.subscreens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.data.network.DeviceStore
import se.hkr.andriod.data.network.UserStore

class UsersViewModelFactory(
    private val userStore: UserStore,
    private val deviceStore: DeviceStore
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(userStore, deviceStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
