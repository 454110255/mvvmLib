package com.example.mvvm.base

interface IBaseViewModel {
    /***
     * 开始请求网络
     */
    fun onBeforRequest()

    /***
     * 网络请求结束
     */
    fun onFinishRequest()

    /***
     * 显示toast
     */
    fun showToast(msg: String)

    /***
     * 登录过期 重新登录
     */
    fun onReLogin()

    /***
     * 登录过期 重新登录
     */
    fun onReLoginForResult(requestCode: Int)

    /***
     * 关闭当前activity
     */
    fun onCloseActivity()


}