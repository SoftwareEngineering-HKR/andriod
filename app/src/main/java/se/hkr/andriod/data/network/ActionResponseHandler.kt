package se.hkr.andriod.data.network

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.json.JSONObject

data class ActionResponse(
    val statusCode: Int,
    val message: String
)

class ActionResponseHandler {

    private val _responses = MutableSharedFlow<ActionResponse>()
    val responses: SharedFlow<ActionResponse> = _responses

    suspend fun handle(json: JSONObject) {
        val payload = json.getJSONObject("payload")

        val response = ActionResponse(
            statusCode = payload.optInt("statusCode"),
            message = payload.optString("message")
        )

        Log.d("ACTION_RESPONSE", "${response.statusCode} - ${response.message}")

        _responses.emit(response)
    }
}
