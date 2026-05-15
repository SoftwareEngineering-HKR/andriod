package se.hkr.andriod.data.network

import android.content.Context
import android.util.Log
import se.hkr.andriod.core.events.ErrorDispatcher

class ConnectionManager(
    private val udpPort: Int = 4444,
    private val errorDispatcher: ErrorDispatcher
) {
    private val udpDiscovery = UdpDiscovery()
    private val webSocketManager = WebSocketManager()

    val deviceStore = DeviceStore(webSocketManager)
    val userStore = UserStore(webSocketManager)
    val roomStore = RoomStore(webSocketManager)
    val actionHandler = ActionResponseHandler(errorDispatcher)

    private val messageRouter = MessageRouter(
        deviceStore,
        userStore,
        roomStore,
        actionHandler
    )

    private var backendIp: String? = null

    // Prevent infinite refresh loops
    private var hasTriedRefresh = false

    private var onAuthFailure: (() -> Unit)? = null

    fun setOnAuthFailureListener(listener: () -> Unit) {
        onAuthFailure = listener
    }

    fun startConnection(onResult: (String?) -> Unit) {
        udpDiscovery.discoverServer(port = udpPort) { ip ->
            if (ip != null) {
                Log.d("CONNECTION", "Backend discovered at $ip")
                backendIp = ip

                onResult(ip)
            } else {
                Log.d("CONNECTION", "Backend discovery failed")
                onResult(null)
            }
        }
    }

    // Connect WebSocket automatically
    fun connectWebSocket(context: Context) {
        val ip = backendIp ?: run {
            Log.d("CONNECTION", "No backend IP available")
            return
        }

        val token = AuthSession.getToken() ?: return

        Log.d("CONNECTION", "Connecting WebSocket")

        hasTriedRefresh = false

        webSocketManager.clearMessageListeners()

        webSocketManager.setOnOpenListener {
            Log.d("CONNECTION", "Socket opened")
        }

        webSocketManager.setOnFailureListener {
            Log.d("CONNECTION", "WebSocket failed")
            handleConnectionLost(context)
        }

        webSocketManager.addMessageListener { message ->
            Log.d("CONNECTION", "Received message: $message")
            messageRouter.handle(message)
        }

        webSocketManager.connect(ip)
    }

    private fun handleConnectionLost(context: Context) {
        if (hasTriedRefresh) {
            Log.d("CONNECTION", "Already tried refresh, giving up")
            return
        }

        hasTriedRefresh = true

        val ip = backendIp ?: return
        val authService = AuthService(context)

        Log.d("CONNECTION", "Refreshing token...")

        authService.refresh(ip) { success, newToken ->

            if (success && newToken != null) {

                AuthSession.saveToken(context, newToken)

                Log.d("CONNECTION", "Refresh success: reconnecting")

                webSocketManager.disconnect()
                connectWebSocket(context)

            } else {
                Log.d("CONNECTION", "Refresh failed")
                onAuthFailure?.invoke()
            }
        }
    }

    fun reconnectWebSocket(context: Context) {
        Log.d("CONNECTION", "Manual reconnect")
        webSocketManager.disconnect()
        connectWebSocket(context)
    }

    fun sendMessage(message: String) {
        Log.d("CONNECTION", "Sending message: $message")
        webSocketManager.sendMessage(message)
    }

    fun updateDeviceValue(deviceId: String, value: Int) {
        Log.d("CONNECTION", "Updating device $deviceId : $value")
        deviceStore.updateDeviceValue(deviceId, value.toString())
    }

    fun disconnect() {
        Log.d("CONNECTION", "Disconnecting from backend")
        webSocketManager.disconnect()
        // Clear stores
        deviceStore.clear()
        userStore.clear()
        roomStore.clear()

        hasTriedRefresh = false // reset for next session
    }

    fun getBackendIp(): String? = backendIp
}
