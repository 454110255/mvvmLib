package com.example.bindingadapter.viewmodel

import androidx.lifecycle.ViewModel

open class ItemViewModel<T> : ViewModel() {
    protected var data: T? = null
    fun bindData(dataModel: T) {
        this.data = dataModel
    }
}
