package com.example.mvvm.okhttp.cookie

import okhttp3.Cookie
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class SerializableOkHttpCookies(cookies: Cookie) : Serializable {
    @Transient
    private val mCookie: Cookie = cookies
    @Transient
    private var clienCookies: Cookie? = null

    fun getCookies(): Cookie = clienCookies ?: mCookie

    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(mCookie.name())
        out.writeObject(mCookie.value())
        out.writeObject(mCookie.expiresAt())
        out.writeObject(mCookie.domain())
        out.writeObject(mCookie.path())
        out.writeObject(mCookie.secure())
        out.writeObject(mCookie.httpOnly())
        out.writeObject(mCookie.hostOnly())
        out.writeObject(mCookie.persistent())
    }

    private fun readObject(input: ObjectInputStream) {
        val name = input.readObject() as String
        val value = input.readObject() as String
        val expiresAt = input.readLong()
        val domain = input.readObject() as String
        val path = input.readObject() as String
        val secure = input.readBoolean()
        val httpOnly = input.readBoolean()
        val hostOnly = input.readBoolean()
        val persistent = input.readBoolean()
        var builder = Cookie.Builder()
        builder.name(name)
            .value(value)
            .expiresAt(expiresAt)
            .path(path)
        builder = if (secure) builder.secure() else builder
        builder = if (httpOnly) builder.httpOnly() else builder
        builder = if (hostOnly) builder.hostOnlyDomain(domain) else builder.domain(domain)
        clienCookies = builder.build()
    }

}