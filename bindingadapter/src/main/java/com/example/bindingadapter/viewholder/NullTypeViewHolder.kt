package com.example.bindingadapter.viewholder

import android.view.View
import com.example.bindingadapter.viewmodel.ItemViewModel

class NullTypeViewHolder<T>(view: View) :
    AbsViewHolder<T, ItemViewModel<T>>(view) {
    override fun generateViewModel(): ItemViewModel<T>? {
        return null
    }
}