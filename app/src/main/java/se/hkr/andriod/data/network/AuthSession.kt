package se.hkr.andriod.data.network

import android.content.Context
import androidx.core.content.edit

object AuthSession {
    private var accessToken: String? = null

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

    fun clear(context: Context) {
        accessToken = null
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean {
        return accessToken != null
    }
}
