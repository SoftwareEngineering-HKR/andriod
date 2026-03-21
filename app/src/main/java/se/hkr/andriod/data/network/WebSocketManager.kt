package se.hkr.andriod.data.network

import android.util.Log
import okhttp3.*

class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null


    fun connect(ip: String, port: Int = 8080) {
        val url = "ws://$ip:$port"
        Log.d("WEBSOCKET", "Connecting to $url")

        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WEBSOCKET", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WEBSOCKET", "Received: $text")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WEBSOCKET", "Closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WEBSOCKET", "Closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("WEBSOCKET", "Failure: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        if (webSocket != null) {
            Log.d("WEBSOCKET", "Sending: $message")
            webSocket?.send(message)
        } else {
            Log.d("WEBSOCKET", "Cannot send message, not connected")
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "App closed")
        webSocket = null
    }
}
