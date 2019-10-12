package com.example.mvvm.okhttp.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class CookieJarMannager(context: Context) : CookieJar {
    private var cookieJar: CookieJarStore = CookieJarStore(context)

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        if (!cookies.isNullOrEmpty()) {
            cookies.forEach {
                cookieJar.add(url, it)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieJar[url]
    }

    /***
     * 清除cookie
     */
    fun clearCookies() {
        cookieJar.removeAll()
    }
}