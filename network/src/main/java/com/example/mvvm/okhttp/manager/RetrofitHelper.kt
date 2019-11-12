package com.example.mvvm.okhttp.manager

import com.example.mvvm.okhttp.config.RetrofitConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.ConcurrentHashMap

class RetrofitHelper private constructor() {
    private val serviceMap = ConcurrentHashMap<String, Retrofit>()
    private lateinit var config: RetrofitConfig

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

    fun initRetrofit(config: RetrofitConfig) {
        this.config = config
    }


    /***
     * 返回实例
     */
    @JvmOverloads
    fun <T> createRequest(serviceCls: Class<T>, host: String): T {
        var retrofit: Retrofit? = null
        if (serviceMap.contains(host)) {
            retrofit = serviceMap[host]
        }
        if (retrofit == null) {
            retrofit = createRetrofit(host)
            serviceMap[host] = retrofit
        }
        return retrofit.create(serviceCls)
    }

    /***
     * 初始化retrofit对象
     */
    @JvmOverloads
    fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClientManager.getOkHttpClient(config))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }
}