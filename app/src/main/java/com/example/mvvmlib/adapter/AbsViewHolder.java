package com.example.mvvmlib.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public abstract class AbsViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context mContext;
    private ViewDataBinding mBinding;
    public AbsViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mContext = itemView.getContext();
    }

    public ViewDataBinding getBinding() {
        return mBinding;
    }

    public abstract void onBindViewHolder(int position, int total, T data);
}
