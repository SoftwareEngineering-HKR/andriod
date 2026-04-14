package se.hkr.andriod.data.network

import android.util.Log

class ConnectionManager(private val udpPort: Int = 4444) {
    private val udpDiscovery = UdpDiscovery()
    private val webSocketManager = WebSocketManager()
    val deviceStore = DeviceStore(webSocketManager) // central device store

    private var isListening = false
    private var backendIp: String? = null

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
    fun connectWebSocket() {
        val ip = backendIp ?: run {
            Log.d("CONNECTION", "No backend IP available")
            return
        }

        webSocketManager.connect(ip)

        // Start listening to messages if not already
        if (!isListening) {
            webSocketManager.addMessageListener { message ->
                Log.d("CONNECTION", "Received message: $message")
                deviceStore.handleIncomingMessage(message)
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
    }
}
