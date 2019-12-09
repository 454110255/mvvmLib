package com.example.mvvmlib.adapter

import android.view.ViewGroup
import com.example.bindingadapter.base.BaseRecyclerAdapter
import com.example.bindingadapter.viewholder.AbsViewHolder
import com.example.mvvmlib.R
import com.example.mvvmlib.model.Data

class FindAdapter :
    BaseRecyclerAdapter<Data>() {
    override fun onFindViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<Data, *>? {
        return FindWithSingleProHolder(inflaterView(R.layout.holder_find_with_single_pro, viewGroup))
    }
}