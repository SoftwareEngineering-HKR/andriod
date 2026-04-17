package se.hkr.andriod.data.network

import android.util.Log
import okhttp3.*

class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val messageListeners = mutableListOf<(String) -> Unit>()

    private var onFailureListener: (() -> Unit)? = null

    fun setOnFailureListener(listener: () -> Unit) {
        onFailureListener = listener
    }

    fun connect(ip: String, port: Int = 8080) {

        val token = AuthSession.getToken()

        val url = if (token != null) {
            "ws://$ip:$port?token=$token"
        } else {
            "ws://$ip:$port"
        }

        Log.d("WEBSOCKET", "Connecting to $url")

        val request = Request.Builder().url(url).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WEBSOCKET", "Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WEBSOCKET", "Received: $text")
                messageListeners.forEach { it(text) }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WEBSOCKET", "Closing: $code / $reason")
                webSocket.close(1000, null)
                onFailureListener?.invoke()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WEBSOCKET", "Closed: $code / $reason")
                onFailureListener?.invoke()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("WEBSOCKET", "Failure: ${t.message}")
                onFailureListener?.invoke()
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

    // Allow external classes to listen for messages
    fun addMessageListener(listener: (String) -> Unit) {
        messageListeners.add(listener)
    }

    fun removeMessageListener(listener: (String) -> Unit) {
        messageListeners.remove(listener)
    }
}
