package se.hkr.andriod.data.network

import android.util.Log

class ConnectionManager(private val udpPort: Int = 4444) {
    private val udpDiscovery = UdpDiscovery()
    private val webSocketManager = WebSocketManager()

    fun startConnection(onResult: (String) -> Unit) {
        udpDiscovery.discoverServer(port = udpPort) { ip ->
            Log.d("CONNECTION", "Backend discovered at $ip")

            // Connect WebSocket automatically
            webSocketManager.connect(ip)

            // Notify caller that backend IP is found
            onResult(ip)
        }
    }

    fun sendMessage(message: String) {
        webSocketManager.sendMessage(message)
    }

    fun disconnect() {
        webSocketManager.disconnect()
    }
}
