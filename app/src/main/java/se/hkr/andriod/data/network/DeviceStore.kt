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

    // Devices assigned to the current user
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> get() = _devices

    // All devices with user relationships (admin)
    private val _allDevices = MutableStateFlow<List<Device>>(emptyList())
    val allDevices: StateFlow<List<Device>> get() = _allDevices

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
                "update device description" -> handleDeviceUpdateDescription(payload)
                "device info" -> handleAllDeviceInfo(payload)
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

            _allDevices.update { currentList ->
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
            _devices.update { currentList -> currentList + device }
            _allDevices.update { currentList -> currentList + device }
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
                    if (device.id == deviceId) device.copy(online = isOnline)
                    else device
                }
            }

            _allDevices.update { currentList ->
                currentList.map { device ->
                    if (device.id == deviceId) device.copy(online = isOnline)
                    else device
                }
            }
        }

        Log.d("DEVICESTORE", "Device online state updated: $deviceId : $isOnline")
    }

    private fun handleDeviceUpdateDescription(payload: JSONObject) {
        val deviceId = payload.optString("deviceID")
        val content = payload.optJSONObject("content") ?: return

        val newName = content.optString("name", "")
        val newDescription = content.optString("description", "")

        if (deviceId.isEmpty()) return

        scope.launch {
            val updateDevice: (Device) -> Device = { device ->
                if (device.id == deviceId) {
                    device.copy(
                        name = newName,
                        description = newDescription
                    )
                } else device
            }

            _devices.update { list -> list.map(updateDevice) }
            _allDevices.update { list -> list.map(updateDevice) }
        }
        Log.d("DEVICESTORE", "Device description updated: $deviceId -> $newName / $newDescription")
    }

    private fun handleAllDeviceInfo(payload: JSONObject) {
        val devicesJson = payload.getJSONArray("devices")
        val allDevicesList = mutableListOf<Device>()

        for (i in 0 until devicesJson.length()) {
            val deviceJson = devicesJson.getJSONObject(i)
            val device = Device.fromBackendJson(deviceJson)
            allDevicesList.add(device)
        }

        scope.launch {
            _allDevices.value = allDevicesList
        }

        Log.d("DEVICESTORE", "All devices loaded: ${allDevicesList.size}")
    }

    // Public helper to send a value update
    fun updateDeviceValue(deviceId: String, value: Int) {
        val message = JSONObject().apply {
            put("type", "update value")
            put("payload", JSONObject().apply {
                put("id", deviceId)
                put("value", value.toString())
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

    // Fetch all devices + relationships (admin)
    fun fetchAllDeviceInfo() {
        val message = JSONObject().apply {
            put("type", "get all device info")
            put("payload", JSONObject())
        }

        webSocketManager.sendMessage(message.toString())
    }
}
