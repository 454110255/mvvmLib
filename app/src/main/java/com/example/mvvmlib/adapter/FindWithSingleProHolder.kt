package com.example.mvvmlib.adapter

import android.view.View
import com.example.bindingadapter.viewholder.AbsViewHolder
import com.example.mvvmlib.model.Data
import com.example.mvvmlib.viewmodel.FindItemViewModel

class FindWithSingleProHolder(view: View) :
    AbsViewHolder<Data, FindItemViewModel>(view) {
    override fun generateViewModel(): FindItemViewModel? {
        return FindItemViewModel()
    }
}