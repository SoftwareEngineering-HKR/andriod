package se.hkr.andriod.data.network

import android.content.Context
import androidx.core.content.edit

object AuthSession {
    private var accessToken: String? = null
    private var username: String? = null

    fun saveSession(context: Context, token: String, username: String) {
        accessToken = token
        this.username = username
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit {
            putString("access_token", token)
            putString("username", username)
        }
    }

    fun loadSession(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        accessToken = prefs.getString("access_token", null)
        username = prefs.getString("username", null)
    }

    fun saveToken(context: Context, token: String) {
        accessToken = token
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit { putString("access_token", token) }
    }

    fun loadToken(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        accessToken = prefs.getString("access_token", null)
    }

    fun getToken(): String? {
        return accessToken
    }

    fun getUsername(): String? {
        return username
    }

    fun clear(context: Context) {
        accessToken = null
        username = null

        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean {
        return accessToken != null
    }
}
