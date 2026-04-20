package se.hkr.andriod.data.network

import android.R.id.message
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import se.hkr.andriod.domain.model.device.Room
import kotlin.collections.filter
import kotlin.collections.map


class RoomStore(private val webSocketManager: WebSocketManager) {

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> get() = _rooms

    private val scope = CoroutineScope(Dispatchers.Main)

    fun handleMessage(json: JSONObject) {
        try {
            val type = json.getString("type")
            val payload = json.getJSONObject("payload")

            when (type.lowercase()) {
                "rooms" -> handleRooms(payload)
                else -> Log.d("ROOMSTORE", "Unhandled message type: $type")
            }
        } catch (e: Exception) {
            Log.e("ROOMSTORE", "Failed to parse message: $message", e)
        }
    }

    private fun handleRooms(payload: JSONObject) {
        try {
            val roomsJsonArray = payload.getJSONArray("rooms")
            val roomsList = mutableListOf<Room>()

            for (i in 0 until roomsJsonArray.length()) {
                val roomObj = roomsJsonArray.getJSONObject(i)
                val room = Room(
                    id = roomObj.getString("id"),
                    name = roomObj.getString("name")
                )
                roomsList.add(room)
            }

            scope.launch {
                _rooms.value = roomsList
            }
        } catch (e: Exception) {
            Log.e("ROOMSTORE", "Failed to parse rooms payload", e)
        }
    }

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

        // Update locally (with temp id)
        val tempId = "temp-${System.currentTimeMillis()}"

        val newRoom = Room(
            id = tempId,
            name = name
        )

        scope.launch {
            _rooms.value += newRoom
        }
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

        // Update locally
        scope.launch {
            _rooms.value = _rooms.value.map { room ->
                if (room.id == id) {
                    room.copy(name = newName)
                } else room
            }
        }
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

        // Update locally
        scope.launch {
            _rooms.value = _rooms.value.filter { it.id != id }
        }
    }
}
