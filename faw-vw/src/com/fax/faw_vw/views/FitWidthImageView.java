package com.fax.faw_vw.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fax.faw_vw.views.clickshow.ClickShowImageView;

/**
 * Created by linfaxin on 2014/8/19 019.
 * Email: linlinfaxin@163.com
 * 填满宽度不变形的ImageView
 */
public class FitWidthImageView extends ImageView {
    public FitWidthImageView(Context context) {
        super(context);
    }

    public FitWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitWidthImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dWidth = getDrawable().getIntrinsicWidth();
        if(dWidth<=0){
        	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        	return;
        }
        int vWidth = MeasureSpec.getSize(widthMeasureSpec);
        float scale = (float) vWidth / (float) dWidth;
        int vHeight = (int) (getDrawable().getIntrinsicHeight() * scale);

        setMeasuredDimension(vWidth, vHeight);
    }


}
