package se.hkr.andriod.data.network

import androidx.lifecycle.ViewModel
import se.hkr.andriod.core.events.ErrorDispatcher

class SharedConnectionViewModel(errorDispatcher: ErrorDispatcher) : ViewModel() {
    val connectionManager = ConnectionManager(errorDispatcher = errorDispatcher)

    override fun onCleared() {
        super.onCleared()
        connectionManager.disconnect()
    }
}
