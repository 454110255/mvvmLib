package com.example.mvvm.okhttp.callback

/***
 * 网络回调
 */
abstract class BaseCallback<T> {

    /***
     * 开始网络请求
     */
    fun onBefore() {}

    /***
     * 网络请求结束
     */
    fun onAfter() {}

    /***
     * token失效
     */
    fun tokenInvalid(){}

    /***
     * 请求响应
     */
    abstract fun onResponse(response: T?, pageCount:Int? = 0, pageNo:Int? = 0)

    abstract fun onError(errorData: String?)

}