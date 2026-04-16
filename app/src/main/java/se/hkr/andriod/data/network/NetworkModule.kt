package se.hkr.andriod.data.network

import android.content.Context
import okhttp3.OkHttpClient

object NetworkModule {
    private var client: OkHttpClient? = null

    fun getClient(context: Context): OkHttpClient {
        if (client == null) {
            client = OkHttpClient.Builder()
                .cookieJar(PersistentCookieJar(context))
                .build()
        }
        return client!!
    }
}
