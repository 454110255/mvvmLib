package com.example.mvvmlib.api

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiTest {
    @POST("sxyp/MiniActiveTemplate")
    @FormUrlEncoded
    fun getData(@Field("versionId") versionId: String): Observable<String>
}