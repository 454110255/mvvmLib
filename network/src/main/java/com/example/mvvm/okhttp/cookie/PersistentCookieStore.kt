/**
 * @file PersistentCookieStore
 * @copyright (c) 2016 Macalline All Rights Reserved.
 * @author SongZheng
 * @date 2016/5/11
 */
package com.example.mvvm.okhttp.cookie

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import java.io.*
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI
import java.net.URISyntaxException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * cookie
 */
open class PersistentCookieStore(context: Context) : CookieStore {

    private val cookies: HashMap<String, ConcurrentMap<String, HttpCookie>>
    private val cookiePrefs: SharedPreferences

    init {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0)
        cookies = HashMap()

        // Load any previously stored cookies into the store
        val prefsMap = cookiePrefs.all

        prefsMap?.forEach { entry ->
            if (entry.value != null && !(entry.value as String).startsWith(COOKIE_NAME_PREFIX)) {
                val cookieNames = TextUtils.split(entry.value as String, ",")
                cookieNames?.forEach { name ->
                    val encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null)
                    if (encodedCookie != null) {
                        val decodeCookie = decodeCookie(encodedCookie)
                        decodeCookie?.also {
                            if (!cookies.containsKey(entry.key)) {
                                cookies[entry.key] = ConcurrentHashMap<String, HttpCookie>()
                            }
                            cookies[entry.key]?.set(name, it)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val COOKIE_PREFS = "CookiePrefsFile"
        private const val COOKIE_NAME_PREFIX = "cookie_"
    }

    override fun add(uri: URI, cookie: HttpCookie) {
        // Save cookie into local store, or remove if expired
        if (!cookie.hasExpired()) {
            if (!cookies.containsKey(cookie.domain))
                cookies[cookie.domain] = ConcurrentHashMap()
            cookies[cookie.domain]?.set(cookie.name, cookie)
        } else {
            if (cookies.containsKey(cookie.domain))
                cookies[cookie.domain]?.remove(cookie.domain)
        }

        // Save cookie into persistent store
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.putString(cookie.domain, TextUtils.join(",", cookies[cookie.domain]!!.keys))
        prefsWriter.putString(
            COOKIE_NAME_PREFIX + cookie.name,
            encodeCookie(SerializableHttpCookie(cookie))
        )
        prefsWriter.commit()
    }

    private fun getCookieToken(uri: URI, cookie: HttpCookie): String {
        return "${cookie.name}${cookie.domain}"
    }

    override fun get(uri: URI): List<HttpCookie> {
        val ret = ArrayList<HttpCookie>()
        for (key in cookies.keys) {
            if (uri.host.contains(key)) {
                cookies[key]?.values?.let { ret.addAll(it) }
            }
        }
        return ret
    }

    override fun removeAll(): Boolean {
        val prefsWriter = cookiePrefs.edit()
        prefsWriter.clear()
        prefsWriter.commit()
        cookies.clear()
        return true
    }


    override fun remove(uri: URI, cookie: HttpCookie): Boolean {
        val name = getCookieToken(uri, cookie)

        if (cookies.containsKey(uri.host) && cookies[uri.host]?.containsKey(name) == true) {
            cookies[uri.host]!!.remove(name)

            val prefsWriter = cookiePrefs.edit()
            if (cookiePrefs.contains(COOKIE_NAME_PREFIX + name)) {
                prefsWriter.remove(COOKIE_NAME_PREFIX + name)
            }
            prefsWriter.putString(uri.host, TextUtils.join(",", cookies[uri.host]!!.keys))
            prefsWriter.commit()

            return true
        } else {
            return false
        }
    }

    override fun getCookies(): List<HttpCookie> {
        val ret = ArrayList<HttpCookie>()
        for (key in cookies.keys)
            cookies[key]?.values?.let { ret.addAll(it) }

        return ret
    }

    override fun getURIs(): List<URI> {
        val ret = ArrayList<URI>()
        for (key in cookies.keys)
            try {
                ret.add(URI(key))
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

        return ret
    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    private fun encodeCookie(cookie: SerializableHttpCookie?): String? {
        if (cookie == null)
            return null
        val os = ByteArrayOutputStream()
        try {
            val outputStream = ObjectOutputStream(os)
            outputStream.writeObject(cookie)
        } catch (e: IOException) {
            return null
        }

        return byteArrayToHexString(os.toByteArray())
    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    protected fun decodeCookie(cookieString: String): HttpCookie? {
        var cookie: HttpCookie? = null
        try {
            val bytes = hexStringToByteArray(cookieString)
            val byteArrayInputStream = ByteArrayInputStream(bytes)
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            val readObject = objectInputStream.readObject()
            if (readObject is SerializableHttpCookie) {
                cookie = readObject.getCookie()
            }
        } catch (e: IOException) {
        } catch (e: ClassNotFoundException) {
        }

        return cookie
    }

    /**
     * Using some super basic byte array <-> hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
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

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        for (i in 0..len step 2) {
            data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character.digit(
                hexString[i + 1],
                16
            )).toByte()
        }
        return data
    }

}
