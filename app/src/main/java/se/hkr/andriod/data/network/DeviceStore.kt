package se.hkr.andriod.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import se.hkr.andriod.domain.model.device.Device
import org.json.JSONObject
import se.hkr.andriod.data.mock.MockDevices

class DeviceStore(private val webSocketManager: WebSocketManager) {

    // TEMPORARY Start with mock devices
    private val _devices = MutableStateFlow<List<Device>>(MockDevices.allDevices)
    val devices: StateFlow<List<Device>> get() = _devices

    // Coroutine scope for updates
    private val scope = CoroutineScope(Dispatchers.Main)

    fun handleIncomingMessage(message: String) {
        try {
            val json = JSONObject(message)
            val type = json.getString("type")
            val payload = json.getJSONObject("payload")

            when (type.lowercase()) {
                "inital devices" -> handleInitialDevices(payload)
                "update value" -> handleDeviceUpdate(payload)
                "action response" -> handleActionResponse(payload)
                else -> Log.d("DEVICESTORE", "Unhandled message type: $type")
            }
        } catch (e: Exception) {
            Log.e("DEVICESTORE", "Failed to parse message: $message", e)
        }
    }

    private fun handleInitialDevices(payload: JSONObject) {
        val devicesJson = payload.getJSONArray("devices")
        val newDevices = mutableListOf<Device>()

        for (i in 0 until devicesJson.length()) {
            val deviceJson = devicesJson.getJSONObject(i)
            val device = Device.fromBackendJson(deviceJson)
            newDevices.add(device)
        }

        scope.launch { _devices.value = newDevices }
        Log.d("DEVICESTORE", "Initial devices loaded: ${newDevices.size}")
    }

    private fun handleDeviceUpdate(payload: JSONObject) {
        val deviceId = payload.optString("deviceID")
        val newValue = payload.optInt("content", -1)
        if (deviceId.isEmpty() || newValue == -1) return

        val updatedList = _devices.value.map { device ->
            if (device.id.toString() == deviceId) device.copy(value = newValue) else device
        }

        scope.launch { _devices.value = updatedList }
        Log.d("DEVICESTORE", "Device updated: $deviceId : $newValue")
    }

    private fun handleActionResponse(payload: JSONObject) {
        val statusCode = payload.optInt("statusCode")
        val message = payload.optString("message")
        Log.d("DEVICESTORE", "Action response: $statusCode - $message")
    }

    // Public helper to send a value update
    fun updateDeviceValue(deviceId: String, value: Int) {
        val message = JSONObject().apply {
            put("type", "update value")
            put("payload", JSONObject().apply {
                put("id", deviceId)
                put("value", value)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Get a device by ID
    fun getDeviceById(id: String): Device? = _devices.value.find { it.id.toString() == id }
}
