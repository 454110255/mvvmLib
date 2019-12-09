package com.example.bindingadapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.example.bindingadapter.BR
import com.example.bindingadapter.R
import com.example.bindingadapter.viewholder.AbsViewHolder
import com.example.bindingadapter.viewholder.NullTypeViewHolder

abstract class BaseRecyclerAdapter<T> :
    RecyclerView.Adapter<AbsViewHolder<T, *>>() {
    protected val mData = ObservableArrayList<T>()
    private var mInflater: LayoutInflater? = null
    var onItemClickListener: OnItemClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsViewHolder<T, *> {
        var holder = onFindViewHolder(parent, viewType)
        return holder ?: NullTypeViewHolder(inflaterView(R.layout.holder_null_type_holder, parent))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: AbsViewHolder<T, *>, position: Int) {
        holder.bindData(mData[position])
        if (holder !is NullTypeViewHolder && onItemClickListener != null) {
            holder.itemBinding?.setVariable(BR.onClickListener, object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onItemClickListener?.onItemClick(
                        holder, holder.getLayoutPosition(),
                        mData[holder.getLayoutPosition()]
                    )
                }

            })
        }
    }

//    override fun onViewAttachedToWindow(holder: AbsViewHolder<T, *>) {
//        super.onViewAttachedToWindow(holder)
//        holder.bind()
//    }
//
//    override fun onViewDetachedFromWindow(holder: AbsViewHolder<T, *>) {
//        super.onViewDetachedFromWindow(holder)
//        holder.unBind()
//    }

    fun inflaterView(@LayoutRes resLayout: Int, parent: ViewGroup): View {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.context)
        }
        return mInflater!!.inflate(resLayout, parent, false)
    }

    /***
     * 添加单一数据
     */
    fun addItemData(data: T) {
        if (data != null && !mData.contains(data)) {
            mData.add(data)
        }
    }

    /***
     * 添加一组数据
     */
    fun addAllData(data: List<T>) {
        if (data.isNullOrEmpty()) return
        mData.addAll(data)
    }

    /***
     * 改变单一数据
     */
    fun setItemData(position: Int, data: T) {
        if (position < 0 || position > mData.size) return
        mData[position] = data
    }

    /***
     * 清空数据
     */
    fun clearData() {
        if (mData.isNotEmpty()) {
            mData.clear()
        }
    }

    /***
     * 替换所有数据
     */
    fun setData(data: List<T>) {
        clearData()
        addAllData(data)
    }

    abstract fun onFindViewHolder(viewGroup: ViewGroup, viewType: Int): AbsViewHolder<T, *>?

}


interface OnItemClickListener<T> {
    fun onItemClick(holder: RecyclerView.ViewHolder, position: Int, data: T)
}
