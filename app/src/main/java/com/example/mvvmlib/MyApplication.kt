package com.example.mvvmlib

import android.app.Application
import com.example.mvvm.okhttp.config.RetrofitConfig
import com.example.mvvm.okhttp.manager.RetrofitHelper
import com.example.mvvmlib.api.ParamsInterceptors

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = RetrofitConfig.initConfig(this) {
            baseUrl = "https://apitest.test.com/"
            isLogInterceptor = BuildConfig.DEBUG
            interceptors = arrayListOf(ParamsInterceptors())
        }
        RetrofitHelper.getInstance().initRetrofit(config)
    }
}