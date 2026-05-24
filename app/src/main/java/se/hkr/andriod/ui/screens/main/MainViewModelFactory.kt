package se.hkr.andriod.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.hkr.andriod.core.events.ErrorDispatcher
import se.hkr.andriod.data.network.ConnectionManager

class MainViewModelFactory(
    private val connectionManager: ConnectionManager
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(connectionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
