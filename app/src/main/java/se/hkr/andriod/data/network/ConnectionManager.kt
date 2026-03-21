package se.hkr.andriod.data.network

import android.util.Log

class ConnectionManager(private val udpPort: Int = 4444) {
    private val udpDiscovery = UdpDiscovery()

    fun startConnection(onResult: (String) -> Unit) {
        udpDiscovery.discoverServer(port = udpPort) { ip ->
            Log.d("CONNECTION", "Backend discovered at $ip")
            onResult(ip)
        }
    }
}
