package com.fax.faw_vw.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fax.faw_vw.views.clickshow.ClickShowImageView;

/**
 * Created by linfaxin on 2014/8/19 019.
 * Email: linlinfaxin@163.com
 * 填满高度不变形的ImageView
 */
public class FitHeightImageView extends ImageView {
    public FitHeightImageView(Context context) {
        super(context);
    }

    public FitHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dHeight = getDrawable().getIntrinsicHeight();
        if(dHeight<=0){
        	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        	return;
        }
        int vHeight = MeasureSpec.getSize(heightMeasureSpec);
        float scale = (float) vHeight / (float) dHeight;
        int vWidth = (int) (getDrawable().getIntrinsicWidth() * scale);

        setMeasuredDimension(vWidth, vHeight);
    }


}
