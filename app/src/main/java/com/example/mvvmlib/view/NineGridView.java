package com.example.mvvmlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mvvmlib.R;

import java.util.ArrayList;
import java.util.List;

public class NineGridView extends ViewGroup {

    public static final int MODE_FILL = 0;          //填充模式，类似于微信
    public static final int MODE_GRID = 1;          //网格模式，类似于QQ，4张图会 2X2布局

    private ImageLoader mImageLoader;        //全局的图片加载器(必须设置,否者不显示图片)

    private int singleImageSize = 250;              // 单张图片时的最大大小,单位dp
    private float singleImageRatio = 1.0f;          // 单张图片的宽高比(宽/高)
    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 3;                    // 宫格间距，单位dp
    private int mode = MODE_FILL;                   // 默认使用fill模式

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    private List<ImageView> imageViews;
    private List mImageInfo;
    private NineGridViewAdapter mAdapter;
    private boolean isClickable;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, dm);
        singleImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, singleImageSize, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        if (a != null) {
            gridSpacing = (int) a.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing);
            singleImageSize = a.getDimensionPixelSize(R.styleable.NineGridView_ngv_singleImageSize, 0);
            singleImageRatio = a.getFloat(R.styleable.NineGridView_ngv_singleImageRatio, singleImageRatio);
            maxImageSize = a.getInt(R.styleable.NineGridView_ngv_maxSize, maxImageSize);
            mode = a.getInt(R.styleable.NineGridView_ngv_mode, mode);
            isClickable = a.getBoolean(R.styleable.NineGridView_ngv_img_clickable, true);
            a.recycle();
        }

        imageViews = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (mImageInfo != null && mImageInfo.size() > 0) {
            if (mImageInfo.size() == 1) {
                height = (int) (width / singleImageRatio);
            } else if (mImageInfo.size() == 3) {
                //三张图片的时候是特殊样式，需要特别处理
                height = (int) ((totalWidth * 2f - gridSpacing) / 3f) + getPaddingBottom() + getPaddingTop();
            } else {
                //这里无论是几张图片，宽高都按总宽度的 1/3
                gridWidth = gridHeight = (totalWidth - gridSpacing * (columnCount - 1)) / columnCount;
                width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
                height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) return;
        int childrenCount = mImageInfo.size();
        if (childrenCount == 1) {
            ImageView childrenView = (ImageView) getChildAt(0);
            int left = getPaddingLeft();
            int top = getPaddingTop();
            int right = getWidth() - getPaddingRight();
            int bottom = getHeight() - getPaddingBottom();
            childrenView.layout(left, top, right, bottom);
            if (mImageLoader != null) {
                mImageLoader.onDisplayImage(getContext(), childrenView, mImageInfo.get(0));
            }
        } else if (childrenCount == 3) {
            //第一张图片的高度
            int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            for (int i = 0; i < childrenCount; i++) {
                ImageView childrenView = (ImageView) getChildAt(i);
                int left, top, right, bottom;
                if (i == 0) {
                    left = getPaddingLeft();
                    top = getPaddingTop();
                    right = viewHeight + left;
                    bottom = viewHeight + top;
                } else {
                    left = getPaddingLeft() + viewHeight + gridSpacing;
                    //item是方形的，宽高相等
                    int itemWidth = getWidth() - left - getPaddingRight();
                    top = getPaddingTop() + (i - 1) * (itemWidth + gridSpacing);
                    right = left + itemWidth;
                    bottom = top + itemWidth;
                }
                childrenView.layout(left, top, right, bottom);
                if (mImageLoader != null) {
                    mImageLoader.onDisplayImage(getContext(), childrenView, mImageInfo.get(i));
                }
            }
        } else {
            for (int i = 0; i < childrenCount; i++) {
                ImageView childrenView = (ImageView) getChildAt(i);

                int rowNum = i / columnCount;
                int columnNum = i % columnCount;
                int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
                int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
                int right = left + gridWidth;
                int bottom = top + gridHeight;
                childrenView.layout(left, top, right, bottom);

                if (mImageLoader != null) {
                    mImageLoader.onDisplayImage(getContext(), childrenView, mImageInfo.get(i));
                }
            }
        }
    }

    /**
     * 设置适配器
     */
    public void setAdapter(@NonNull NineGridViewAdapter adapter) {
        mAdapter = adapter;
        List imageInfo = adapter.getImageInfo();

        if (imageInfo == null || imageInfo.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imageInfo.size();
        if (maxImageSize > 0 && imageCount > maxImageSize) {
            imageInfo = imageInfo.subList(0, maxImageSize);
            imageCount = imageInfo.size();   //再次获取图片数量
        }

        //默认是3列显示，行数根据图片的数量决定
        rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
        columnCount = 3;
        //grid模式下，显示4张使用2X2模式
        if (mode == MODE_GRID) {
            if (imageCount == 4) {
                rowCount = 2;
                columnCount = 2;
            } else if (imageCount == 2) {
                rowCount = 1;
                columnCount = 2;
            }
        }

        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView iv = getImageView(i);
                if (iv == null) return;
                addView(iv, generateDefaultLayoutParams());
            }
        } else {
            int oldViewCount = mImageInfo.size();
            int newViewCount = imageCount;
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) return;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        mImageInfo = imageInfo;
        requestLayout();
    }

    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */
    private ImageView getImageView(final int position) {
        ImageView imageView;
        if (position < imageViews.size()) {
            imageView = imageViews.get(position);
        } else {
            imageView = mAdapter.generateImageView(getContext());
            if (isClickable) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.onImageItemClick(getContext(), NineGridView.this, position, mAdapter.getImageInfo());
                    }
                });
            }
            imageViews.add(imageView);
        }
        return imageView;
    }

    /**
     * 设置宫格间距
     */
    public void setGridSpacing(int spacing) {
        gridSpacing = spacing;
    }

    /**
     * 设置只有一张图片时的宽
     */
    public void setSingleImageSize(int maxImageSize) {
        singleImageSize = maxImageSize;
    }

    /**
     * 设置只有一张图片时的宽高比
     */
    public void setSingleImageRatio(float ratio) {
        singleImageRatio = ratio;
    }

    /**
     * 设置最大图片数
     */
    public void setMaxSize(int maxSize) {
        maxImageSize = maxSize;
    }

    public int getMaxSize() {
        return maxImageSize;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setImageClickable(boolean isClickable) {
        isClickable = isClickable;
    }

    public interface ImageLoader<T> {
        /**
         * 需要子类实现该方法，以确定如何加载和显示图片
         *
         * @param context   上下文
         * @param imageView 需要展示图片的ImageView
         * @param data      传进来的数据
         */
        void onDisplayImage(Context context, ImageView imageView, T data);
    }
}
