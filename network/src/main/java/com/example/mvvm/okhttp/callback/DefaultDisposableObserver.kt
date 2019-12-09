package com.example.mvvm.okhttp.callback

import android.text.TextUtils
import com.example.mvvm.constants.ResponseConstants
import com.example.mvvm.okhttp.model.HttpResult
import com.google.gson.JsonSyntaxException
import io.reactivex.observers.DisposableObserver
import java.net.ConnectException
import java.net.SocketTimeoutException

/***
 * 对网络结果进行分发
 */
class DefaultDisposableObserver<T, R>(private val callback: BaseCallback<R>?) :
    DisposableObserver<T>() {
    override fun onComplete() {
        callback?.let {
            it.onAfter()
        }
    }

    override fun onNext(t: T) {
        callback?.let { callback ->
            when (t) {
                null -> callback.onError("网络错误")
                is HttpResult<*> -> {
                    when {
                        TextUtils.equals(ResponseConstants.CODE_TOKEN_INVALID.toString(), t.status) -> {
                            callback.tokenInvalid()
                        }
                        TextUtils.equals(ResponseConstants.CODE_SUCCESS.toString(), t.status) -> {
                            callback.onResponse(t.data as? R, t.pageCount, t.pageNo)
                        }
                        else -> {
                            callback.onError(t.statusMessage?:t.statusDetail)
                        }
                    }
                }
                else -> {
                    callback.onResponse(t as? R)
                }
            }
        }

    }

    override fun onError(e: Throwable) {
        callback?.let {
            it.onAfter()
            when (e) {
                is SocketTimeoutException, is ConnectException -> it.onError("网络不给力,请检查您的网络状态")
                is JsonSyntaxException -> it.onError("数据解析异常  ${e.message}")
                else -> it.onError("网络错误")
            }
        }
    }
}