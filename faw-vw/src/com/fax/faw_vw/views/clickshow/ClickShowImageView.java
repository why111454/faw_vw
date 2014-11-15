package com.fax.faw_vw.views.clickshow;

import com.fax.faw_vw.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by linfaxin on 2014/7/26 026.
 * Email: linlinfaxin@163.com
 */
public class ClickShowImageView extends ImageView{
    public ClickShowImageView(Context context) {
        super(context);
    }

    public ClickShowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickShowImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
		super.setPressed(pressed);
        invalidate();
	}

    @SuppressLint("NewApi")
	@Override
    public void draw(Canvas canvas) {
        if(isPressed() || (VERSION.SDK_INT>=11 && isActivated())){
            setColorFilter(getContext().getResources().getColor(R.color.alpha_black));
        }else setColorFilter(null);
        super.draw(canvas);
    }

}
