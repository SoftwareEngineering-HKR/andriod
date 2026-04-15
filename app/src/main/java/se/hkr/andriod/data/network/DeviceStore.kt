package se.hkr.andriod.data.network

import android.R.id.message
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import se.hkr.andriod.domain.model.device.Device

class DeviceStore(private val webSocketManager: WebSocketManager) {

    // Start with empty list
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> get() = _devices

    // Coroutine scope for updates
    private val scope = CoroutineScope(Dispatchers.Main)

    fun handleMessage(json: JSONObject) {
        try {
            val type = json.getString("type")
            val payload = json.getJSONObject("payload")

            when (type.lowercase()) {
                "inital devices" -> handleInitialDevices(payload)
                "update value" -> handleDeviceUpdate(payload)
                "added new device" -> handleAddedNewDevice(payload)
                "update device onlinestate" -> handleDeviceOnlineState(payload)
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

        scope.launch {
            _devices.update { currentList ->
                currentList.map { device ->
                    if (device.id == deviceId) device.copy(value = newValue)
                    else device
                }
            }
        }

        Log.d("DEVICESTORE", "Device updated: $deviceId : $newValue")
    }

    private fun handleAddedNewDevice(payload: JSONObject) {
        val deviceJson = payload.optJSONObject("content") ?: return
        val device = Device.fromBackendJson(deviceJson)

        scope.launch {
            _devices.update { currentList ->
                // Append the new device
                currentList + device
            }
        }

        Log.d("DEVICESTORE", "New device added: ${device.id}")
    }

    private fun handleDeviceOnlineState(payload: JSONObject) {
        val deviceId = payload.optString("deviceID")
        if (deviceId.isEmpty()) return

        val isOnline = payload.optBoolean("content")

        scope.launch {
            _devices.update { currentList ->
                currentList.map { device ->
                    if (device.id == deviceId) {
                        device.copy(online = isOnline)
                    } else device
                }
            }
        }

        Log.d("DEVICESTORE", "Device online state updated: $deviceId : $isOnline")
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

    // Update a device name and description
    fun updateDevice(deviceId: String, name: String, description: String) {
        _devices.update { currentList ->
            currentList.map { device ->
                if (device.id == deviceId) {
                    device.copy(
                        name = name,
                        description = description
                    )
                } else device
            }
        }

        val message = JSONObject().apply {
            put("type", "update device")
            put("payload", JSONObject().apply {
                put("id", deviceId)
                put("name", name)
                put("description", description)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Delete a device
    fun deleteDevice(deviceId: String) {
        _devices.update { currentList ->
            currentList.filter { it.id != deviceId }
        }

        val message = JSONObject().apply {
            put("type", "delete device")
            put("payload", JSONObject().apply {
                put("id", deviceId)
            })
        }

        webSocketManager.sendMessage(message.toString())
    }

    // Get a device by ID
    fun getDeviceById(id: String): Device? = _devices.value.find { it.id == id }
}
