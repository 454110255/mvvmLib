package com.example.mvvmlib.viewmodel

import com.example.bindingadapter.viewmodel.ItemViewModel
import com.example.mvvmlib.model.Data

class FindItemViewModel : ItemViewModel<Data>() {
    fun getAvatar(): String? {
        return data?.avatar
    }

    fun getUserName(): String? {
        return data?.userName
    }

    fun getReleaseTime(): String? {
        return data?.releaseTime
    }

    fun getTitle(): String? {
        return data?.title
    }

    fun getDigest(): String? {
        return data?.digest
    }
}