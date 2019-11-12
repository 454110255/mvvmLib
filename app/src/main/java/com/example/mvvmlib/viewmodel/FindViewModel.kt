package com.example.mvvmlib.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSONObject
import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvmlib.model.FindBean
import com.example.mvvmlib.repository.FindRepository

class FindViewModel : ViewModel() {
    private val repository = FindRepository()
    private val pageCount = 1
    private var pageNo = 1

    fun getFindList() {
        repository.getFindList(pageCount, pageNo, object : BaseCallback<String>() {
            override fun onResponse(response: String?) {
                val data = response?.let {
                    val findBean = JSONObject.parseObject(response, FindBean::class.java)
                    findBean
                }

                data?.let { data ->

                }
            }

            override fun onError(errorData: String?) {
                Log.w("tag", "  onError----->$errorData")
            }

        })
    }
}