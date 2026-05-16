package se.hkr.andriod.data.network

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import se.hkr.andriod.R
import java.io.IOException

class AuthService(private val context: Context) {
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
                onResult(false, context.getString(R.string.error_cannot_connect))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful) {
                    val message = try {
                        JSONObject(responseBody ?: "").getString("message")
                    } catch (e: Exception) {
                        null
                    }
                    onResult(false, message ?: context.getString(R.string.error_request_failed))
                    return
                }

                if (responseBody == null) {
                    onResult(false, context.getString(R.string.error_empty_response))
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

        postRequest(url, json) { success, result ->

            if (!success) {
                val error = if (result == context.getString(R.string.error_cannot_connect)) {
                    result
                } else {
                    context.getString(R.string.error_invalid_credentials)
                }
                onResult(false, error)
                return@postRequest
            }

            try {
                val jsonResponse = JSONObject(result!!)
                val token = jsonResponse.getString("accessToken")

                AuthSession.saveSession(
                    context = context,
                    token = token,
                    username = username
                )

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

        postRequest(url, json) { success, result ->

            if (!success) {
                onResult(false, result ?: context.getString(R.string.error_register_failed))
                return@postRequest
            }

            try {
                val jsonResponse = JSONObject(result!!)
                val token = jsonResponse.getString("accessToken")

                AuthSession.saveSession(
                    context = context,
                    token = token,
                    username = username
                )

                onResult(true, token)

            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun logout(
        ip: String,
        accessToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val url = "http://$ip:8081/logout"

        val json = JSONObject()

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(json.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                onResult(false, context.getString(R.string.error_cannot_connect))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful) {
                    val message = try {
                        JSONObject(responseBody ?: "").getString("message")
                    } catch (e: Exception) {
                        null
                    }
                    onResult(false, message ?: context.getString(R.string.error_logout_failed))
                    return
                }

                onResult(true, context.getString(R.string.logout_success))
            }
        })
    }

    fun refresh(
        ip: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val url = "http://$ip:8081/refresh"

        val request = Request.Builder()
            .url(url)
            .post("".toRequestBody(null)) // empty body
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                onResult(false, context.getString(R.string.error_cannot_connect))
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (!response.isSuccessful) {
                    val message = try {
                        JSONObject(responseBody ?: "").getString("message")
                    } catch (e: Exception) {
                        null
                    }
                    onResult(false, message ?: context.getString(R.string.error_refresh_failed))
                    return
                }

                try {
                    val json = JSONObject(responseBody!!)
                    val newToken = json.getString("accessToken")

                    AuthSession.saveToken(context, newToken)

                    onResult(true, newToken)

                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            }
        })
    }
}
