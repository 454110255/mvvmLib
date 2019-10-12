package com.example.mvvm.okhttp.interceptor

import android.text.TextUtils
import android.util.Log
import okhttp3.*

open class DefaultParamsInterceptor : Interceptor {

    protected open fun getDefaultParams(): Map<String, String>? = null

    /***
     * 添加header
     */
    protected open fun getDefaultHeader(): Map<String, String>? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val params = getDefaultParams()
        if (!params.isNullOrEmpty()) {
            when (request.method()) {
                "GET" -> {
                    request = addGetDefaultParams(request, params)
                }
                "POST" -> {
                    val body = request.body()
                    if (body is FormBody) {
                        request = addFormDefaultParams(request, body, params)
                    } else if (body is MultipartBody && MultipartBody.FORM == body.type()) {
                        request = addMultipartDefaultParams(request, body, params)
                    }
                }
            }
        }
        val headers = getDefaultHeader()
        if (!headers.isNullOrEmpty()) {
            request = addHeaderDefaultParams(request, headers)
        }
        return chain.proceed(request)
    }

    private fun addGetDefaultParams(request: Request, params: Map<String, String>): Request {
        val builder = request.url().newBuilder()
        params.forEach {
            builder.addQueryParameter(it.key, it.value)
        }
        return request.newBuilder().url(builder.build()).build()
    }

    private fun addMultipartDefaultParams(
        request: Request,
        oldBody: MultipartBody,
        params: Map<String, String>
    ): Request {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val parts = oldBody.parts()
        if (!parts.isNullOrEmpty()) {
            parts.forEach {
                builder.addPart(it)
            }
        }
        params.forEach {
            builder.addFormDataPart(it.key, it.value)
        }
        return request.newBuilder().post(builder.build()).build()
    }

    private fun addFormDefaultParams(
        request: Request,
        oldFormBody: FormBody,
        params: Map<String, String>
    ): Request {
        val builder = FormBody.Builder()
        params.forEach {
            builder.add(it.key, it.value)
        }

        if (oldFormBody.size() > 0) {
            for (i in 0 until oldFormBody.size()) {
                builder.add(oldFormBody.name(i), oldFormBody.value(i))
            }
        }
        return request.newBuilder().post(builder.build()).build()
    }


    private fun addHeaderDefaultParams(request: Request, headers: Map<String, String>): Request {
        val builder = request.newBuilder()
        headers.forEach {
            builder.addHeader(it.key, it.value)
        }
        return builder.build()
    }

}