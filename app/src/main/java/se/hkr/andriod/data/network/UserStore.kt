package se.hkr.andriod.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

data class User(
    val id: String,
    val username: String,
    val role: String
)

class UserStore(private val webSocketManager: WebSocketManager) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val scope = CoroutineScope(Dispatchers.Main)

    fun handleMessage(json: JSONObject) {
        val type = json.getString("type").lowercase()
        val payload = json.getJSONObject("payload")

        when (type) {
            "users" -> handleUsers(payload)
        }
    }

    private fun handleUsers(payload: JSONObject) {
        val usersJson = payload.optJSONArray("users") ?: return
        val newUsers = mutableListOf<User>()

        for (i in 0 until usersJson.length()) {
            val userJson = usersJson.getJSONObject(i)

            val user = User(
                id = userJson.optString("id"),
                username = userJson.optString("username"),
                role = userJson.optString("type")
            )

            newUsers.add(user)
        }

        scope.launch { _users.value = newUsers }
        Log.d("USERSTORE", "Users loaded: ${newUsers.size}")
    }
}
