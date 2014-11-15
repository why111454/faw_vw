package com.fax.faw_vw.views.clickshow;

import com.fax.faw_vw.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by linfaxin on 2014/7/26 026.
 * Email: linlinfaxin@163.com
 * 必须要设置background属性才有效果
 */
public class ClickShowFrameLayout extends FrameLayout{
    public ClickShowFrameLayout(Context context) {
        super(context);
        setClickable(true);
        init();
    }

    public ClickShowFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        init();
    }

    public ClickShowFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setClickable(true);
        init();
    }
    private void init(){
    	if(getBackground()==null){
    		setBackgroundResource(android.R.color.transparent);
    	}
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
