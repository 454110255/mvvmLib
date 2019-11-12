package com.example.mvvmlib.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mvvmlib.R;
import com.example.mvvmlib.model.DiscoveryArticleImg;
import com.example.mvvmlib.view.NineGridView;

public class FindImageLoader implements NineGridView.ImageLoader<DiscoveryArticleImg> {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, DiscoveryArticleImg data) {
        if (imageView == null) return;
        if (data == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(data.getImage()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
        }
    }
}
