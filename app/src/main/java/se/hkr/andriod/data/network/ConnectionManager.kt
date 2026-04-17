package se.hkr.andriod.data.network

import android.content.Context
import android.util.Log

class ConnectionManager(private val udpPort: Int = 4444) {
    private val udpDiscovery = UdpDiscovery()
    private val webSocketManager = WebSocketManager()

    val deviceStore = DeviceStore(webSocketManager)
    val userStore = UserStore(webSocketManager)
    val actionHandler = ActionResponseHandler()

    private val messageRouter = MessageRouter(
        deviceStore,
        userStore,
        actionHandler
    )

    private var isListening = false
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

        val token = AuthSession.getToken()

        if (token == null) {
            Log.d("CONNECTION", "No token available, cannot connect")
            return
        }

        Log.d("CONNECTION", "Connecting WebSocket with token")

        webSocketManager.connect(ip)

        // Failure message listener
        webSocketManager.setOnFailureListener {
            Log.d("CONNECTION", "WebSocket failed")

            if (!hasTriedRefresh) {
                Log.d("CONNECTION", "Trying refresh...")

                hasTriedRefresh = true

                val authService = AuthService(context)

                authService.refresh(ip) { success, newToken ->
                    if (success && newToken != null) {
                        Log.d("CONNECTION", "Refresh successful, retrying connection")

                        AuthSession.saveToken(context, newToken)
                        connectWebSocket(context) // retry
                    } else {
                        Log.d("CONNECTION", "Refresh failed, user must log in again")
                        onAuthFailure?.invoke()
                    }
                }
            } else {
                Log.d("CONNECTION", "Already tried refresh, giving up")
            }
        }

        // Normal message listener
        if (!isListening) {
            webSocketManager.addMessageListener { message ->
                Log.d("CONNECTION", "Received message: $message")
                messageRouter.handle(message)
            }
            isListening = true
        }
    }

    fun sendMessage(message: String) {
        Log.d("CONNECTION", "Sending message: $message")
        webSocketManager.sendMessage(message)
    }

    fun updateDeviceValue(deviceId: String, value: Int) {
        Log.d("CONNECTION", "Updating device $deviceId : $value")
        deviceStore.updateDeviceValue(deviceId, value)
    }

    fun disconnect() {
        Log.d("CONNECTION", "Disconnecting from backend")
        webSocketManager.disconnect()
        isListening = false
        hasTriedRefresh = false // reset for next session
    }

    fun getBackendIp(): String? {
        return backendIp
    }
}
