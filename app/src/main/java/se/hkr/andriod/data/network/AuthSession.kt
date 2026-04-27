package se.hkr.andriod.data.network

import android.content.Context
import androidx.core.content.edit
import org.json.JSONObject
import android.util.Base64
import se.hkr.andriod.domain.model.user.User
import se.hkr.andriod.domain.model.user.UserRole
import java.util.UUID

object AuthSession {
    private var accessToken: String? = null
    private var username: String? = null
    private var userId: UUID? = null
    private var role: String? = null

    fun saveSession(context: Context, token: String, username: String) {
        accessToken = token
        this.username = username

        // Extract userId and role from JWT
        val parsed = parseToken(token)
        userId = parsed?.first
        role = parsed?.second

        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit {
            putString("access_token", token)
            putString("username", username)
            putString("user_id", userId?.toString())
            putString("role", role)
        }
    }

    fun loadSession(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        accessToken = prefs.getString("access_token", null)
        username = prefs.getString("username", null)

        userId = prefs.getString("user_id", null)?.let {
            try { UUID.fromString(it) } catch (_: Exception) { null }
        }

        role = prefs.getString("role", null)
    }

    fun saveToken(context: Context, token: String) {
        accessToken = token

        // Re-parse token on refresh
        val parsed = parseToken(token)
        userId = parsed?.first
        role = parsed?.second

        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit {
            putString("access_token", token)
            putString("user_id", userId?.toString())
            putString("role", role)
        }
    }

    fun loadToken(context: Context) {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        accessToken = prefs.getString("access_token", null)

        userId = prefs.getString("user_id", null)?.let {
            try { UUID.fromString(it) } catch (_: Exception) { null }
        }

        role = prefs.getString("role", null)
    }

    fun getToken(): String? {
        return accessToken
    }

    fun getUsername(): String? {
        return username
    }

    fun getUserId(): UUID? {
        return userId
    }

    fun getRole(): String? {
        return role
    }

    fun getUser(): User {
        val role = UserRole.fromBackendType(role ?: "user")

        return User(
            id = userId ?: error("No userId in session"),
            username = username ?: "",
            role = role
        )
    }

    fun clear(context: Context) {
        accessToken = null
        username = null
        userId = null
        role = null

        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit { clear() }
    }

    fun isLoggedIn(): Boolean {
        return accessToken != null
    }

    private fun parseToken(token: String): Pair<UUID, String>? {
        return try {
            val parts = token.split(".")
            val payload = parts[1]

            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedString = String(decodedBytes)

            val json = JSONObject(decodedString)

            val userId = UUID.fromString(json.getString("sub"))
            val role = json.getString("role")

            Pair(userId, role)

        } catch (e: Exception) {
            null
        }
    }
}
