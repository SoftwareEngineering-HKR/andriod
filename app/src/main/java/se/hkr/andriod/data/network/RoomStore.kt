package se.hkr.andriod.data.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

data class Room(
    val id: String,
    val name: String
)

class RoomStore(private val webSocketManager: WebSocketManager) {

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> get() = _rooms

    private val scope = CoroutineScope(Dispatchers.Main)

    // Get rooms
    fun getRooms() {
        val message = JSONObject().apply {
            put("type", "get all rooms")
            put("payload", JSONObject())
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Create room
    fun createRoom(name: String) {
        val message = JSONObject().apply {
            put("type", "create room")
            put("payload", JSONObject().apply {
                put("name", name)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Update room name
    fun updateRoomName(id: String, newName: String) {
        val message = JSONObject().apply {
            put("type", "update room")
            put("payload", JSONObject().apply {
                put("id", id)
                put("name", newName)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Delete room
    fun deleteRoom(id: String) {
        val message = JSONObject().apply {
            put("type", "delete room")
            put("payload", JSONObject().apply {
                put("id", id)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }
}
