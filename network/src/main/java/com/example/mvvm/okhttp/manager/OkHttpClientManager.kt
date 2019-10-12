package com.example.mvvm.okhttp.manager

import com.example.mvvm.okhttp.config.RetrofitConfig
import com.example.mvvm.okhttp.cookie.CookieJarMannager
import com.example.mvvm.okhttp.utils.LogUtil
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientManager private constructor() {
    companion object {
        private var mClient: OkHttpClient? = null
        @JvmStatic
        fun getOkHttpClient(config: RetrofitConfig): OkHttpClient {
            return mClient ?: synchronized(OkHttpClientManager::class) {
                mClient ?: let {
                    initClient(config)
                    mClient!!
                }
            }
        }

        /***
         * 初始化OkHttpClient
         */
        private fun initClient(config: RetrofitConfig) {
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(config.readTimeOut, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(config.writeTimeOut, TimeUnit.SECONDS)//写入超时时间

            //cookie
            if (config.isSupportCookie) {
                builder.cookieJar(config.cookieManager ?: CookieJarMannager(config.context))
            }

            //缓存
            if (config.isSupportCache){
                builder.cache(Cache(config.cacheDirector, config.cacheSize))
            }

            //拦截器
            if (!config.interceptors.isNullOrEmpty()) {
                config.interceptors!!.forEach {
                    builder.addInterceptor(it)
                }
            }

            if (config.isLogInterceptor) {
                builder.addInterceptor(getHttpLoggingInterceptor())
            }
            mClient = builder.build()
        }

        /***
         * 日志拦截器
         */
        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                LogUtil.d(it)
            })

            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    }


}