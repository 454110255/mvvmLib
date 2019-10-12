package com.example.mvvm.okhttp.model

import com.example.mvvm.okhttp.annotations.Poko

@Poko
data class HttpResult<T> constructor(
    val status: String,//接口状态
    val statusMessage: String,//接口message
    val pageCount: Int,
    val pageNo: Int,
    val data: T
)