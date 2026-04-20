package se.hkr.andriod.data.network

import android.R.id.message
import android.util.Log
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
        // TODO
    }

    // Create room
    fun createRoom(name: String) {
        // TODO
    }

    // Update room name
    fun updateRoomName(roomId: String, newName: String) {
        // TODO
    }

    // Delete room
    fun deleteRoom(roomId: String) {
        // TODO
    }
}
