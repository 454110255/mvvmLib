package com.example.mvvmlib.view;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public abstract class NineGridViewAdapter<T> implements Serializable {

    protected Context context;
    private List<T> imageInfo;

    public NineGridViewAdapter(Context context, List<T> imageInfo) {
        this.context = context;
        this.imageInfo = imageInfo;
    }

    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     *
     * @param context      上下文
     * @param nineGridView 九宫格控件
     * @param index        当前点击图片的的索引
     * @param imageInfo    图片地址的数据集合
     */
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<T> imageInfo) {
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    protected ImageView generateImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public List<T> getImageInfo() {
        return imageInfo;
    }

    public void setImageInfoList(List<T> imageInfo) {
        this.imageInfo = imageInfo;
    }
}