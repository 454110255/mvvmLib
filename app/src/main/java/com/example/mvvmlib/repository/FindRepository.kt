package com.example.mvvmlib.repository

import com.example.mvvm.base.BaseRepository
import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvm.okhttp.config.RetrofitConfig
import com.example.mvvmlib.api.ApiTest

class FindRepository : BaseRepository() {
    private val service = getService(ApiTest::class.java)

    override fun getBaseUrl(): String {
        return "https://sxypproducttest.sxfqsc.com/"
    }

    fun getFindList(pageCount: Int, pageNo: Int, callback: BaseCallback<String>) {
        execute(service.getFindList(pageCount, pageNo), callback)
    }

}