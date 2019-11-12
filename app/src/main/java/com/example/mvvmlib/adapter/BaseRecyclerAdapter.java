package com.example.mvvmlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<AbsViewHolder> {
    protected Context mContext;
    private LayoutInflater mInflater;
    protected ObservableArrayList<T> mDataList;
    private OnItemClickListener mClickListener;

    public BaseRecyclerAdapter(Context context, ObservableArrayList<T> data) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList = data;
    }

    public void setOnItemClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    public AbsViewHolder<T> onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return onFindViewHolder(viewGroup, viewType);
    }

    public abstract @NotNull
    AbsViewHolder onFindViewHolder(@NonNull ViewGroup viewGroup, int viewType);

    @Override
    public void onBindViewHolder(@NonNull AbsViewHolder absViewHolder, int position) {
        if (mDataList == null || position >= mDataList.size()) return;
        T data = mDataList.get(position);
        if (data != null) {
            absViewHolder.onBindViewHolder(position, mDataList.size(), data);
        }
    }


    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public List<T> getData() {
        return mDataList;
    }

    /***
     * 添加数据源
     * @param data
     */
    public void addItemData(T data) {
        if (mDataList == null) {
            mDataList = new ObservableArrayList<>();
        }
        if (data != null && !mDataList.contains(data)) {
            mDataList.add(data);
        }
    }

    /***
     * 添加一组数据
     * @param data
     */
    public void addAllData(List<? extends T> data) {
        if (data == null) return;
        if (mDataList == null) {
            mDataList = new ObservableArrayList<>();
        }
        mDataList.addAll(data);
    }

    /***
     * 设置单条数据
     * @param postion
     * @param data
     */
    public void setItemData(int postion, T data) {
        if (mDataList == null || postion > mDataList.size()) return;
        mDataList.set(postion, data);
    }

    /***
     * 清空所有数据
     */
    public void clearData() {
        if (mDataList != null && !mDataList.isEmpty()) {
            mDataList.clear();
        }
    }

    /***
     * 更新数据集
     * @param data
     */
    public void setData(ObservableArrayList data) {
        this.mDataList = data;
    }

    /***
     * 加载布局
     * @param resLayout
     * @param parent
     * @return
     */
    public View inflaterView(int resLayout, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(mContext);
        }
        return mInflater.inflate(resLayout, parent, false);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(RecyclerView.ViewHolder holder, int position, T data);
    }

}
