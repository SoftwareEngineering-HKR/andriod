package se.hkr.andriod.data.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthService {
    private val client = OkHttpClient()

    fun login(
        ip: String,
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val url = "http://$ip:8081/login"

        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val body = json.toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                onResult(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody == null) {
                    onResult(false, "Login failed")
                    return
                }

                try {
                    val jsonResponse = JSONObject(responseBody)
                    val token = jsonResponse.getString("accessToken")

                    AuthSession.saveToken(token)

                    onResult(true, token)

                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            }
        })
    }
}
