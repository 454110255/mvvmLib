package com.example.mvvm.okhttp.model

data class HttpResult<T> constructor(
    val status: String?,//接口状态
    val statusMessage: String?,//接口message
    val statusDetail: String?,//接口message
    val pageCount: Int?,
    val pageNo: Int?,
    val data: T?
)