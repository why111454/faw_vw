package com.fax.faw_vw.util;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class SimpleDirectionGesture extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{
    public static final int Direction_None=0;
    public static final int Direction_Up=1;
    public static final int Direction_Down=2;
    public static final int Direction_Left=3;
    public static final int Direction_Right=4;
    View view;
    GestureDetector gestureDetector;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
    public SimpleDirectionGesture(View view){
    	this.view = view;
        gestureDetector = new GestureDetector(view.getContext(), this);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return true;//receive the motion event
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();
        
        if(Math.abs(x) >= Math.abs(y)){//gesture left or right
        	int limit = view.getWidth()/12;//限制必须得划过1/12才能算划过
            if(x > limit || x < -limit){
                if(x>0) onFling(Direction_Right);
                else if(x<=0) onFling(Direction_Left);
            }
        }else{//gesture down or up
        	int limit = view.getHeight()/12;//限制必须得划过1/12才能算划过
            if(y > limit || y < -limit){
                if(y>0)  onFling(Direction_Down);
                else if(y<=0) onFling(Direction_Up);
            }
        }
        return true;
    }
    public abstract void onFling(int direction);
}