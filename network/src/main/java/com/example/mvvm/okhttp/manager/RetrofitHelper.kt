package com.example.mvvm.okhttp.manager

import com.example.mvvm.okhttp.config.RetrofitConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitHelper private constructor() {
    private lateinit var retrofit: Retrofit

    companion object {
        private var INSTANCE: RetrofitHelper? = null
        @JvmStatic
        fun getInstance(): RetrofitHelper {
            return INSTANCE ?: synchronized(RetrofitHelper::class) {
                INSTANCE ?: let {
                    val helper = RetrofitHelper()
                    INSTANCE = helper
                    helper
                }
            }
        }
    }

    /***
     * 返回实例
     */
    fun <T> createRequest(serviceCls: Class<T>): T {
        return retrofit.create(serviceCls)
    }

    /***
     * 初始化retrofit对象
     */
    @JvmOverloads
    fun initRetrofit(config: RetrofitConfig) {
        retrofit = Retrofit.Builder()
            .client(OkHttpClientManager.getOkHttpClient(config))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(config.baseUrl)
            .build()
    }
}