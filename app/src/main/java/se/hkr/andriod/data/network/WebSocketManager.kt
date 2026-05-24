package se.hkr.andriod.data.network

import android.util.Log
import okhttp3.*

class WebSocketManager {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val messageListeners = mutableListOf<(String) -> Unit>()

    private var onFailureListener: (() -> Unit)? = null
    private var onOpenListener: (() -> Unit)? = null

    // Prevent stale socket callbacks
    private var currentSocketId = 0

    private var manualDisconnect = false

    fun setOnFailureListener(listener: () -> Unit) {
        onFailureListener = listener
    }

    fun setOnOpenListener(listener: () -> Unit) {
        onOpenListener = listener
    }

    fun connect(ip: String, port: Int = 8080) {
        manualDisconnect = false
        currentSocketId++
        val socketId = currentSocketId

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
                if (socketId != currentSocketId) return
                Log.d("WEBSOCKET", "Connected")
                onOpenListener?.invoke()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                if (socketId != currentSocketId) return
                Log.d("WEBSOCKET", "Received: $text")
                messageListeners.forEach { it(text) }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WEBSOCKET", "Closing: $code / $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (socketId != currentSocketId) return

                Log.d("WEBSOCKET", "Closed: $code / $reason")

                if (!manualDisconnect) {
                    onFailureListener?.invoke()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                if (socketId != currentSocketId) return
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

    fun clearMessageListeners() {
        messageListeners.clear()
    }

    fun disconnect() {
        manualDisconnect = true
        currentSocketId++
        webSocket?.close(1000, "App closed")
        webSocket = null
        clearMessageListeners()
    }

    // Allow external classes to listen for messages
    fun addMessageListener(listener: (String) -> Unit) {
        messageListeners.add(listener)
    }

    fun removeMessageListener(listener: (String) -> Unit) {
        messageListeners.remove(listener)
    }
}
