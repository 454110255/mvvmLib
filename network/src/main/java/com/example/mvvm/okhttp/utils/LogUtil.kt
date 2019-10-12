package com.example.mvvm.okhttp.utils

import android.util.Log
import androidx.annotation.Nullable
import com.example.mvvm.BuildConfig

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * 日志打印类
 */
object LogUtil {

    private const val LOG_TAG = "sxyp"

    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            //                .methodOffset(2)
            .tag(LOG_TAG)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, @Nullable tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    fun printLog(log: String) {
        if (BuildConfig.DEBUG) {
            val clazzName2 = Throwable().stackTrace[1].className
            val funcName2 = Throwable().stackTrace[1].methodName
            Log.e("$clazzName2--$funcName2()##########", "jsonStr = $log")
        }
    }

    fun v(msg: String, vararg args: Any) {
        logcat(Logger.VERBOSE, LOG_TAG, msg, *args)
    }

    fun v(tag: String, msg: String, vararg args: Any) {
        logcat(Logger.VERBOSE, tag, msg, *args)
    }

    fun d(msg: Any) {
        Logger.d(msg)
    }

    fun d(tag: String, msg: Any) {
        Logger.t(tag).d(msg)
    }

    fun d(tag: String, msg: String, vararg args: Any) {
        logcat(Logger.DEBUG, tag, msg, *args)
    }

    fun i(msg: String, vararg args: Any) {
        logcat(Logger.INFO, LOG_TAG, msg, *args)
    }

    fun i(tag: String, msg: String, vararg args: Any) {
        logcat(Logger.INFO, tag, msg, *args)
    }

    fun w(msg: String, vararg args: Any) {
        logcat(Logger.WARN, LOG_TAG, msg, *args)
    }

    fun w(tag: String, msg: String, vararg args: Any) {
        logcat(Logger.WARN, tag, msg, *args)
    }

    fun e(msg: String, vararg args: Any) {
        logcat(Logger.ERROR, LOG_TAG, msg, *args)
    }

    fun e(tag: String, msg: String, vararg args: Any) {
        logcat(Logger.ERROR, tag, msg, *args)
    }

    fun loggerXml(xml: String) {
        Logger.xml(xml)
    }

    fun loggerXml(tag: String, xml: String) {
        Logger.t(tag).xml(xml)
    }

    fun loggerJson(tag: String, json: String) {
        Logger.t(tag).json(json)
    }

    @JvmOverloads
    private fun logcat(logLevel: Int = Logger.DEBUG, tag: String = LOG_TAG, msg: String, vararg args: Any) {
        when (logLevel) {
            Logger.VERBOSE -> Logger.t(tag).v(msg, *args)
            Logger.DEBUG -> Logger.t(tag).d(msg, *args)
            Logger.INFO -> Logger.t(tag).i(msg, *args)
            Logger.WARN -> Logger.t(tag).w(msg, *args)
            Logger.ERROR -> Logger.t(tag).e(msg, *args)
            Log.ASSERT -> Logger.t(tag).wtf(msg, *args)
            else -> {
            }
        }
    }

}
