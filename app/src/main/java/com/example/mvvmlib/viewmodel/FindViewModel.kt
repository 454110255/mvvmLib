package com.example.mvvmlib.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.mvvm.okhttp.callback.BaseCallback
import com.example.mvvmlib.adapter.FindAdapter
import com.example.mvvmlib.model.FindBean
import com.example.mvvmlib.repository.FindRepository

class FindViewModel : ViewModel() {
    private val repository = FindRepository()
    private val pageCount = 10
    private var pageNo = 1
    var mAdapter: FindAdapter? = null

    init {
        mAdapter = FindAdapter()
    }

    fun onClick(view: View) {
        getFindList()
    }

    fun getFindList() {
        repository.getFindList(pageCount, pageNo, object : BaseCallback<FindBean>() {
            override fun onResponse(response: FindBean?, pageCount: Int?, pageNo: Int?) {
                response?.let { data ->
                    val list = data.data
                    if (!list.isNullOrEmpty()) {
                        mAdapter?.addAllData(list)
                        mAdapter?.notifyDataSetChanged()
                    }
                }


            }

            override fun onError(errorData: String?) {
                Log.w("tag", "  onError----->$errorData")
            }

        })
    }
}