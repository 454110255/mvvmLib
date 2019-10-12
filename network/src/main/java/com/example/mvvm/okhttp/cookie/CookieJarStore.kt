package com.example.mvvm.okhttp.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class CookieJarStore(context: Context) {
    private val cookiePref = "cookie_prefs"
    private val cookies: HashMap<String, ConcurrentMap<String, Cookie>> = HashMap()
    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = context.getSharedPreferences(cookiePref, Context.MODE_PRIVATE)

        //storage the cookie in memory
        val prefsMap = cookiePrefs.all
        prefsMap?.forEach { entry ->
            val cookieNames = TextUtils.split(entry.value?.toString(), ",")
            cookieNames?.forEach { name ->
                val encodedCookie = cookiePrefs.getString(name, null)
                if (encodedCookie != null) {
                    val decodeCookie = decodeCookie(encodedCookie)
                    decodeCookie?.also {
                        if (!cookies.containsKey(entry.key)) {
                            cookies[entry.key] = ConcurrentHashMap<String, Cookie>()
                        }
                        cookies[entry.key]?.set(name, it)
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String {
        return "${cookie.name()}@${cookie.domain()}"
    }

    fun add(url: HttpUrl, cookie: Cookie) {
        val token = getCookieToken(cookie)

        //storage the cookie in memory. If past due. reset cookie
        val host = url.host()
        if (!cookie.persistent()) {
            if (!cookies.containsKey(host)) {
                cookies[host] = ConcurrentHashMap<String, Cookie>()
            }
            cookies[host]?.remove(token)
        } else {
            if (cookies.containsKey(host)) {
                cookies[host]?.remove(token)
            }
        }

        val values = cookies[host]
        values?.also {
            val prefsWriter = cookiePrefs.edit()
            prefsWriter.putString(host, TextUtils.join(",", it.keys))
            prefsWriter.putString(token, encodeCookie(SerializableOkHttpCookies(cookie)))
            prefsWriter.apply()
        }
    }

    private fun encodeCookie(cookie: SerializableOkHttpCookies?): String? {
        if (cookie == null) {
            return null
        }
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            return null
        }
        return byteArrayToHexString(os.toByteArray())
    }

    /***
     * 清除所有cookie
     */
    fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.apply()
        cookies.clear()
        return true
    }

    /***
     * 删除cookie
     */
    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val token = getCookieToken(cookie)
        if (cookies.containsKey(url.host()) && cookies[url.host()]?.containsKey(token) == true) {
            cookies[url.host()]?.remove(token)

            val editor = cookiePrefs.edit()
            if (cookiePrefs.contains(token)) {
                editor.remove(token)
            }
            editor.putString(url.host(), TextUtils.join(",", cookies[url.host()]?.keys))
            editor.apply()
            return true
        }

        return false
    }

    operator fun get(url: HttpUrl): MutableList<Cookie> {
        val ret = ArrayList<Cookie>()
        if (cookies.containsKey(url.host()))
            cookies[url.host()]?.values?.let { ret.addAll(it) }
        return ret
    }

    /***
     * 获取所有cookie
     */
    fun getCookies(): List<Cookie> {
        val result = ArrayList<Cookie>()
        for (entry in cookies) {
            entry.value?.values?.let { result.addAll(it) }
        }
        return result
    }

    /***
     * 将字符串反序列化成cookies
     */
    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = hexStringToByteArray(cookieString)
        var cookie: Cookie? = null
        try {
            val inputStream = ByteArrayInputStream(bytes)
            val objectInputStream = ObjectInputStream(inputStream)
            val readObject = objectInputStream.readObject()
            if (readObject is SerializableOkHttpCookies) {
                cookie = readObject.getCookies()
            }
        } catch (e: IOException) {

        } catch (e: ClassNotFoundException) {

        }
        return cookie
    }

    private fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v = element.toInt() and 0xff
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        for (i in 0..len step 2) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(
                hexString[i + 1],
                16
            )).toByte()
        }
        return data
    }

}

