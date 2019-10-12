package com.example.mvvmlib.api

import com.example.mvvm.okhttp.interceptor.DefaultParamsInterceptor

/***
 * 添加默认参数
 */
class ParamsInterceptors : DefaultParamsInterceptor() {
    override fun getDefaultParams(): Map<String, String>? {
        return mapOf("a" to "b")
    }

    override fun getDefaultHeader(): Map<String, String>? {
        return super.getDefaultHeader()
    }
}