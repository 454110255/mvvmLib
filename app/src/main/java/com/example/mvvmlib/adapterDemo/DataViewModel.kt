package com.example.mvvmlib.adapterDemo

import androidx.databinding.Bindable

class DataViewModel {
    @get:Bindable
    private val adapter: DataAdapter = DataAdapter()
    private val data: MutableList<DataModel>

    init {
        data = ArrayList()
    }

    fun setUp() {
        //执行注册任务，比如说adding listeners

    }

    fun tearDown() {
        //执行注销任务，比如removing listeners
    }

    private fun populateData(){
        
    }


}