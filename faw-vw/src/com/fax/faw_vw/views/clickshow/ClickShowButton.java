package com.fax.faw_vw.views.clickshow;

import com.fax.faw_vw.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by linfaxin on 2014/7/26 026.
 * Email: linlinfaxin@163.com
 */
public class ClickShowButton extends Button{
    public ClickShowButton(Context context) {
        super(context);
    }

    public ClickShowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickShowButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPressed(boolean pressed) {
		super.setPressed(pressed);
        invalidate();
	}

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isPressed()){
            canvas.drawColor(getContext().getResources().getColor(R.color.alpha_black));
        }
    }

}
