package com.example.xyzreader.ui.images;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * It seems we set height of an image as width / aspect_ratio.
 * We don't use this class - we rather need all images be the same
 * 1.5 aspect ratio. In fact all images have 1.5 ratio except 2 of them.
 * */
public class DynamicHeightNetworkImageView extends NetworkImageView {
    private float mAspectRatio = 1.5f;

    public DynamicHeightNetworkImageView(Context context) {
        super(context);
    }
    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAspectRatio(float aspectRatio) {
        mAspectRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }
}
