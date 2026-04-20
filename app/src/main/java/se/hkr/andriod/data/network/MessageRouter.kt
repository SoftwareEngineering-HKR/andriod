package se.hkr.andriod.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class MessageRouter(
    private val deviceStore: DeviceStore,
    private val userStore: UserStore,
    private val RoomStore: RoomStore,
    private val actionHandler: ActionResponseHandler
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    fun handle(message: String) {
        try {
            val json = JSONObject(message)
            when (val type = json.getString("type").lowercase()) {

                // Device messages
                "inital devices",
                "update value",
                "added new device",
                "update device onlinestate",
                "device info" -> deviceStore.handleMessage(json)

                // User messages
                "users" -> userStore.handleMessage(json)

                // Action response
                "action response" -> {
                    scope.launch {
                        actionHandler.handle(json)
                    }
                }

                else -> Log.d("ROUTER", "Unhandled type: $type")
            }

        } catch (e: Exception) {
            Log.e("ROUTER", "Failed to route message", e)
        }
    }
}
