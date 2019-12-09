package com.example.bindingadapter.viewholder

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.bindingadapter.BR
import com.example.bindingadapter.viewmodel.ItemViewModel

abstract class AbsViewHolder<T, VM : ItemViewModel<T>>(view: View) :
    RecyclerView.ViewHolder(view) {
    val mContext: Context = itemView.context
    private var itemViewModel: ItemViewModel<T>? = generateViewModel()
    var itemBinding: ViewDataBinding? = null

    init {
        bind()
    }

    fun bind() {
        if (itemBinding == null) {
            itemBinding = DataBindingUtil.bind(itemView)
        }
    }

    fun unBind() {
        itemBinding?.unbind()
    }

    fun bindData(dataModel: T) {
        itemViewModel?.let { viewModel ->
            viewModel.bindData(dataModel)
            itemBinding?.setVariable(BR.viewModel, itemViewModel)
        }
    }

    abstract fun generateViewModel(): VM?

}