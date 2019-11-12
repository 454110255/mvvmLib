package com.example.mvvm.okhttp.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.mvvm.okhttp.config.RetrofitConfig

fun isNetworkConnected(): Boolean {
    val manager =
        RetrofitConfig.application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = manager.activeNetworkInfo
    info != null && info.isConnected
    return true
}