package com.fax.utils.view;

import com.fax_utils.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**类似ActionBar，可以方便集成使用的一个TopBarView */
@SuppressLint("NewApi")
public class TopBarContain extends LinearLayout {
	public static int DefaultBgResId = android.R.color.black;//默认背景
	
    protected LinearLayout topbar;
    protected View contentView;
    protected TextView titleTv;
    protected Button leftBtn;
    protected Button rightBtn;
    protected ProgressBar progressBar;
    protected LinearLayout rightExtraWidget;
	public TopBarContain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TopBarContain(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TopBarContain(Context context) {
		super(context);
		init();
	}
	private void init(){
		setOrientation(VERTICAL);
		
		topbar= (LinearLayout) View.inflate(getContext(), R.layout.topbar, null);
		topbar.setBackgroundResource(DefaultBgResId);
		
		titleTv=(TextView)topbar.findViewById(R.id.topbar_title);
		leftBtn=(Button) topbar.findViewById(R.id.topbar_left_btn);
		rightBtn=(Button) topbar.findViewById(R.id.topbar_right_btn);
        rightExtraWidget = (LinearLayout) topbar.findViewById(R.id.topbar_right_extra_widget);
		addView(topbar, 0);
		progressBar=(ProgressBar) topbar.findViewById(R.id.topbar_progress);
	}
	public View getTopBar(){
		return topbar;
	}
	
	public TopBarContain setBackgroundRes(int resId){
		topbar.setBackgroundResource(resId);
		return this;
	}
	
	public TopBarContain setTitle(int resId){
		return setTitle(getContext().getString(resId));
	}
	public TopBarContain setTitle(CharSequence title){
		titleTv.setText(title);
		return this;
	}
	public TopBarContain setTitleBg(int resId){
		titleTv.setBackgroundResource(resId);
		return this;
	}
	public TopBarContain setTitle(CharSequence title, int drawableLeft, int drawableRight){
		titleTv.setText(title);
		titleTv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, 0, drawableRight, 0);
		return this;
	}
	public TopBarContain setProgress(int progress){
		progressBar.setProgress(progress);
		if(progress>0&&progress<100) progressBar.setVisibility(View.VISIBLE);
		else progressBar.setVisibility(View.INVISIBLE);
		return this;
	}
	public TopBarContain setLeftCacel(){
        return setLeftFinish(getContext().getString(android.R.string.cancel), 0);
    }
	public TopBarContain setLeftFinish(String btn, int drawableResId){
		return setLeftBtn(btn, drawableResId, new OnClickListener() {
			@Override
			public void onClick(View v) {
				Context context=getContext();
                if(context instanceof FragmentActivity){
                    if(!((FragmentActivity) context).getSupportFragmentManager().popBackStackImmediate()){
                        ((FragmentActivity) context).finish();
                    }
                }else if(context instanceof Activity){
                    if(VERSION.SDK_INT>=11 && !((Activity) context).getFragmentManager().popBackStackImmediate()){
                        ((FragmentActivity) context).finish();
                    }else ((Activity)context).finish();
                }
			}
		});
	}
	public TopBarContain setLeftBackStack(String btn, int drawableResId,final FragmentManager fm){
		return setLeftBtn(btn, drawableResId, new OnClickListener() {
			@Override
			public void onClick(View v) {
				fm.popBackStack();
			}
		});
	}
	public TopBarContain setLeftBtn(String btn, OnClickListener clickListener){
		return setLeftBtn(btn, 0, clickListener);
	}
	public TopBarContain setLeftBtn(int drawableResId, OnClickListener clickListener){
		return setLeftBtn(null, drawableResId, clickListener);
	}
	public TopBarContain setLeftBtn(String btn, int drawableResId, OnClickListener clickListener){
		leftBtn.setText(btn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
		leftBtn.setOnClickListener(clickListener);
		return this;
	}
	public TopBarContain setRightBtn(String btn, OnClickListener clickListener){
		return setRightBtn(btn, 0, clickListener);
	}
	public TopBarContain setRightBtn(int drawableResId, OnClickListener clickListener){
		return setRightBtn(null, drawableResId, clickListener);
	}
	public TopBarContain setRightBtn(String btn, int drawableResId, OnClickListener clickListener){
		rightBtn.setText(btn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableResId, 0);
		rightBtn.setOnClickListener(clickListener);
		return this;
	}
	public TopBarContain addRightIcon(int drawableRes, OnClickListener listener){
        ImageButton icon = new ImageButton(getContext());
        int padding = (int) (10 * getContext().getResources().getDisplayMetrics().density);
        icon.setPadding(padding, 0, padding, 0);
        icon.setImageResource(drawableRes);
        if(Build.VERSION.SDK_INT>=16) icon.setBackground(rightBtn.getBackground().getConstantState().newDrawable());
        else icon.setBackgroundDrawable(rightBtn.getBackground().getConstantState().newDrawable());
        return addRightExtraView(icon, listener);
    }
    public TopBarContain addRightExtraView(View child, OnClickListener listener){
        child.setOnClickListener(listener);
        rightExtraWidget.addView(child, -2, -1);
        return this;
    }
	public TopBarContain setContentView(int resId){
		return setContentView(View.inflate(getContext(), resId, null));
	}
	public TopBarContain setContentView(View view){
        contentView=view;
		removeAllViews();
		addView(topbar);
		addView(view, -1, -1);
		return this;
	}
	public View getContentView(){
        return contentView;
    }

	/**在topBar底下再加一个View，共用TopBar的底色 */
    public TopBarContain addViewInner(View view){
        topbar.addView(view);
        return this;
    }
}
