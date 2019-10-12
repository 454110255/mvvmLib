package com.example.mvvm.okhttp.cookie

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.net.HttpCookie

class SerializableHttpCookie(cookie: HttpCookie) : Serializable {
    @Transient
    private val mCookie: HttpCookie = cookie
    @Transient
    private var clientCookie: HttpCookie? = null

    fun getCookie(): HttpCookie = clientCookie ?: mCookie

    private fun writeObject(out: ObjectOutputStream) {
        out.writeObject(mCookie.name)
        out.writeObject(mCookie.value)
        out.writeObject(mCookie.comment)
        out.writeObject(mCookie.commentURL)
        out.writeObject(mCookie.domain)
        out.writeLong(mCookie.maxAge)
        out.writeObject(mCookie.path)
        out.writeObject(mCookie.portlist)
        out.writeInt(mCookie.version)
        out.writeBoolean(mCookie.secure)
        out.writeBoolean(mCookie.discard)
    }

    private fun readObject(inStream: ObjectInputStream) {
        val name = inStream.readObject() as String
        val value = inStream.readObject() as String
        clientCookie = HttpCookie(name, value)
        clientCookie!!.comment = inStream.readObject() as String
        clientCookie!!.commentURL = inStream.readObject() as String
        clientCookie!!.domain = inStream.readObject() as String
        clientCookie!!.maxAge = inStream.readLong()
        clientCookie!!.path = inStream.readObject() as String
        clientCookie!!.portlist = inStream.readObject() as String
        clientCookie!!.version = inStream.readInt()
        clientCookie!!.secure = inStream.readBoolean()
        clientCookie!!.discard = inStream.readBoolean()
    }
}