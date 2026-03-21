package se.hkr.andriod.data.network

import android.util.Log
import se.hkr.andriod.utils.DeviceUtils
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UdpDiscovery {
    fun discoverServer(port: Int = 4444, onResult: (String?) -> Unit) {
        Thread {
            try {
                if (DeviceUtils.isEmulator()) {
                    // Emulator: skip UDP broadcast
                    val ip = "10.0.2.2"
                    Log.d("UDP", "Running on emulator, using IP: $ip")
                    onResult(ip)
                    return@Thread
                }

                // Real device: send UDP broadcast
                val socket = DatagramSocket()
                socket.broadcast = true

                // Time out after 5 seconds
                socket.soTimeout = 5000

                val message = "NetworkDiscovery;Ver=1;"
                val buffer = message.toByteArray()

                val packet = DatagramPacket(
                    buffer,
                    buffer.size,
                    InetAddress.getByName("255.255.255.255"),
                    port
                )

                Log.d("UDP", "Sending UDP discovery to 255.255.255.255:$port")
                socket.send(packet)

                val responseBuffer = ByteArray(1024)
                val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)

                Log.d("UDP", "Waiting for UDP response...")
                socket.receive(responsePacket)

                val serverIp = responsePacket.address.hostAddress
                val responseMessage = String(responsePacket.data, 0, responsePacket.length)
                Log.d("UDP", "Received UDP response from $serverIp: $responseMessage")

                socket.close()
                if (serverIp != null) {
                    onResult(serverIp)
                }

            } catch (e: java.net.SocketTimeoutException) {
                Log.d("UDP", "UDP discovery timed out")
                onResult(null)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult("Error: ${e.message}")
            }
        }.start()
    }
}
