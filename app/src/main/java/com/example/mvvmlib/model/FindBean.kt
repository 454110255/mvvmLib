package com.example.mvvmlib.model

data class FindBean(
    val apiStatusType: String?,
    val `data`: List<Data>?,
    val pageCount: Int?,
    val pageNo: Int?,
    val status: Int?,
    val statusMessage: String?
)

data class Data(
    val avatar: String?,
    val digest: String?,
    val id: Int?,
    val no: String?,
    val releaseTime: String?,
    val releaseTimeStamp: String?,
    val title: String?,
    val userLoginName: String?,
    val userName: String?
)