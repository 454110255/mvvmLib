package com.example.mvvmlib.repository

import com.example.mvvm.base.BaseRepository
import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvmlib.api.ApiTest
import com.example.mvvmlib.model.FindBean

class FindRepository : BaseRepository() {
    private val service = getService(ApiTest::class.java)

    override fun getBaseUrl(): String {
        return "https://sx.com/"
    }

    fun getFindList(pageCount: Int, pageNo: Int, callback: BaseCallback<FindBean>) {
        execute(service.getFindList(pageCount, pageNo), callback)
    }

}