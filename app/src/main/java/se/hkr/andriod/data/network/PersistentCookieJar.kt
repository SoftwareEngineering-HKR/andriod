package se.hkr.andriod.data.network

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import org.json.JSONArray
import androidx.core.content.edit

class PersistentCookieJar(context: Context) : CookieJar {
    private val prefs = context.getSharedPreferences("cookies", Context.MODE_PRIVATE)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val storedCookies = loadAllCookies().toMutableList()

        cookies.forEach { newCookie ->
            storedCookies.removeAll {
                it.name == newCookie.name && it.domain == newCookie.domain
            }
            storedCookies.add(newCookie)
        }
        saveAllCookies(storedCookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies = loadAllCookies()

        return cookies.filter { it.matches(url) }
    }

    fun clear() {
        prefs.edit { clear() }
    }

    private fun saveAllCookies(cookies: List<Cookie>) {
        val jsonArray = JSONArray()

        cookies.forEach { cookie ->
            val json = org.json.JSONObject().apply {
                put("name", cookie.name)
                put("value", cookie.value)
                put("domain", cookie.domain)
                put("path", cookie.path)
                put("expiresAt", cookie.expiresAt)
                put("secure", cookie.secure)
                put("httpOnly", cookie.httpOnly)
            }
            jsonArray.put(json)
        }
        prefs.edit { putString("cookie_list", jsonArray.toString()) }
    }

    private fun loadAllCookies(): List<Cookie> {
        val jsonString = prefs.getString("cookie_list", null) ?: return emptyList()

        val jsonArray = JSONArray(jsonString)
        val cookies = mutableListOf<Cookie>()

        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)

            val cookie = Cookie.Builder()
                .name(json.getString("name"))
                .value(json.getString("value"))
                .domain(json.getString("domain"))
                .path(json.getString("path"))
                .expiresAt(json.getLong("expiresAt"))
                .apply {
                    if (json.getBoolean("secure")) secure()
                    if (json.getBoolean("httpOnly")) httpOnly()
                }
                .build()

            cookies.add(cookie)
        }
        return cookies
    }
}
