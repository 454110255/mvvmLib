package com.example.mvvmlib.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.example.mvvmlib.R
import com.example.mvvmlib.databinding.HolderFindWithSingleProBinding
import com.example.mvvmlib.model.Data

class FindAdapter(context: Context, data: ObservableArrayList<Data>) :
    BaseRecyclerAdapter<Data>(context, data) {
    override fun onFindViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<*> {
        val bind = DataBindingUtil.bind<HolderFindWithSingleProBinding>(
            inflaterView(
                R.layout.holder_find_with_single_pro,
                viewGroup
            )
        )
        return FindWithSingleProHolder(bind!!)
    }
}