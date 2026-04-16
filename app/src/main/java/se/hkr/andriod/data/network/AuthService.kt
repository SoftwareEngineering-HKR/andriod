package se.hkr.andriod.data.network

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AuthService(context: Context) {
    private val client = NetworkModule.getClient(context)

    private fun postRequest(
        url: String,
        jsonBody: JSONObject,
        onResult: (Boolean, String?) -> Unit
    ) {
        val body = jsonBody.toString()
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
                    onResult(false, "Request failed")
                    return
                }

                onResult(true, responseBody)
            }
        })
    }

    fun login(
        ip: String,
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val url = "http://$ip:8081/login"

        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        postRequest(url, json) { success, response ->

            if (!success || response == null) {
                onResult(false, "Login failed")
                return@postRequest
            }

            try {
                val jsonResponse = JSONObject(response)
                val token = jsonResponse.getString("accessToken")

                AuthSession.saveToken(token)

                onResult(true, token)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun register(
        ip: String,
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val url = "http://$ip:8081/signup"

        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        postRequest(url, json) { success, response ->

            if (!success || response == null) {
                onResult(false, "Register failed")
                return@postRequest
            }

            try {
                val jsonResponse = JSONObject(response)
                val token = jsonResponse.getString("accessToken")

                AuthSession.saveToken(token)

                onResult(true, token)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
