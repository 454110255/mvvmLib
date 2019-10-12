package com.example.mvvm.okhttp.config

import android.app.Application
import com.example.mvvm.okhttp.cookie.CookieJarMannager
import okhttp3.Interceptor
import java.io.File

class RetrofitConfig (val context: Application) {
    //基础域名
    var baseUrl: String? = null
    //是否输出日志
    var isLogInterceptor: Boolean = false
    //是否debug模式
    var isDebugModel: Boolean = false

    /**okhttp配置信息*/
    //连接、读写超时时间，单位(秒)
    var readTimeOut: Long = 30
    var connectTimeout: Long = 15
    var writeTimeOut: Long = 20
    //cookies
    var cookieManager: CookieJarMannager? = null
    var isSupportCookie: Boolean = false
    //缓存区大小 默认20M
    var cacheSize: Long = 20 * 1024 * 1024
    //缓存目录
    var cacheDirector: File? = context.cacheDir
    var isSupportCache: Boolean = true
    var interceptors: List<Interceptor>? = null

    init {
        application = context
    }

    companion object {
        lateinit var  application:Application
        @JvmStatic
        @JvmOverloads
        inline fun initConfig(
            context: Application,
            block: RetrofitConfig.() -> Unit = {}
        ): RetrofitConfig = RetrofitConfig(context).apply(block)
    }
}