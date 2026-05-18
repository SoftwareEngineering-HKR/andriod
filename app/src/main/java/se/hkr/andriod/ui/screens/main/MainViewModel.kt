package se.hkr.andriod.ui.screens.main

import android.content.Context
import androidx.lifecycle.ViewModel
import se.hkr.andriod.core.events.ErrorDispatcher
import se.hkr.andriod.data.network.AuthSession
import se.hkr.andriod.data.network.ConnectionManager

class MainViewModel(
    val connectionManager: ConnectionManager
) : ViewModel() {
    private var isInitialized = false

    fun initConnection(context: Context) {
        if (isInitialized) return
        isInitialized = true

        val token = AuthSession.getToken()
        if (token != null) {
            connectionManager.startConnection { ip ->
                if (ip != null) {
                    connectionManager.connectWebSocket(context)
                }else {
                    // Trigger logout if discovery fails
                    connectionManager.triggerAuthFailure()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        connectionManager.disconnect()
    }
}
