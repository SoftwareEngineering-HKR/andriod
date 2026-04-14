package se.hkr.andriod.data.network

object AuthSession {
    private var accessToken: String? = null

    fun saveToken(token: String) {
        accessToken = token
    }

    fun getToken(): String? {
        return accessToken
    }

    fun clear() {
        accessToken = null
    }

    fun isLoggedIn(): Boolean {
        return accessToken != null
    }
}
