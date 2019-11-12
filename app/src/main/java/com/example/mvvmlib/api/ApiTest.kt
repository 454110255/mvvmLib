package com.example.mvvmlib.api

import com.example.mvvm.okhttp.model.HttpResult
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiTest {
    @POST("miniActiveTemplateNew")
    @FormUrlEncoded
    fun getData(@Field("versionId") versionId: String): Observable<HttpResult<String>>

    @FormUrlEncoded
    @POST("discoveryArticle/list")
    fun getFindList(@Field("pageCount") pageCount:Int, @Field("pageNo") pageNo:Int):Observable<String>
}