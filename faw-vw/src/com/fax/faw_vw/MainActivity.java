package com.fax.faw_vw;

import java.util.ArrayList;
import java.util.List;

import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_main.BrandFragment;
import com.fax.faw_vw.fragments_main.FindCarsAssistorFragment;
import com.fax.faw_vw.fragments_main.HomeFragment;
import com.fax.faw_vw.fragments_main.MoreFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.menu.QRFragment;
import com.fax.faw_vw.menu.SearchAppFragment;
import com.fax.faw_vw.model.ImageResPagePair;
import com.fax.faw_vw.model.ImageTextPagePair;
import com.fax.faw_vw.more.FeedbackFragment;
import com.fax.faw_vw.more.MenuListSettingFragment;
import com.fax.faw_vw.more.OnlineQAFragment;
import com.fax.faw_vw.more.PersonFragment;
import com.fax.faw_vw.more.QueryIllegalFragment;
import com.fax.faw_vw.util.Blur;
import com.fax.faw_vw.views.AppDialogBuilder;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameAnimListener;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.view.RadioGroupFragmentBinder;
import com.fax.utils.view.RadioGroupStateFragmentBinder;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	
	ObjectXListView menuList;
	@Override
	protected void onResume() {
		super.onResume();
		menuList.reload();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final DrawerLayout drawerLayout = (DrawerLayout) View.inflate(this, R.layout.activity_main, null);
		setContentView(drawerLayout);
		
		//初始化侧边栏
		View.OnClickListener animRightDrawClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				animDrawableRight((TextView) v);
			}
		};
		findViewById(R.id.main_menu_btn_tel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String tel = ((TextView)v).getText().toString();
				startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+tel)));
			}
		});
		findViewById(R.id.main_menu_btn_search).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentContain.start(MainActivity.this, SearchAppFragment.class);
			}
		});
		//初始化侧边栏里可以被配置的条目
		menuList = (ObjectXListView) findViewById(android.R.id.list);
		menuList.setAdapter(new ObjectXAdapter.SingleLocalPageAdapter<ImageTextPagePair>() {
			@Override
			public View bindView(ImageTextPagePair t, int position, View convertView) {
				TextView tv = (TextView) convertView;
				if(tv==null){
					tv = new TextView(MainActivity.this);
					int padding = (int) MyApp.convertToDp(12);
					tv.setPadding(padding, padding, padding, padding);
					tv.setBackgroundResource(R.drawable.common_btn_in_black);
					tv.setCompoundDrawablePadding((int) MyApp.convertToDp(12));
					tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
					tv.setMinHeight((int) MyApp.convertToDp(50));
					tv.setTextColor(Color.WHITE);
					tv.setTextSize(20);
				}
				tv.setText(t.getText());
				tv.setCompoundDrawablesWithIntrinsicBounds(t.getImgResId(), 0, 0, 0);
				return tv;
			}
			@Override
			public List<ImageTextPagePair> instanceNewList() throws Exception {
				return MenuListSettingFragment.getEnablePagePairs(MainActivity.this);
			}
			@Override
			public boolean isDynamicHeight() {
				return true;
			}
			@Override
			public void onItemClick(ImageTextPagePair t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);
				if(t.getFragment()!=null){
					if(t.isLandscape()){
						FragmentContainLandscape.start(MainActivity.this, t.getFragment());
					}else{
						FragmentContain.start(MainActivity.this, t.getFragment());
					}
				}
			}
		});
		
		
		//初始化模糊效果
		drawerLayout.setScrimColor(Color.TRANSPARENT);
		final View contextView = drawerLayout.getChildAt(0);
		final ImageView frontView = (ImageView) contextView.findViewById(android.R.id.background);
		drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			@SuppressLint("NewApi")
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				if(Build.VERSION.SDK_INT>=11) frontView.setAlpha(slideOffset);
			}
			Bitmap blurBitmap;
			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
				if(newState == DrawerLayout.STATE_DRAGGING){
					if(blurBitmap==null) 
						blurBitmap = Bitmap.createBitmap(contextView.getWidth()/4 , contextView.getHeight()/4 ,Bitmap.Config.ARGB_8888);
					try {
						Canvas canvas = new Canvas(blurBitmap);
						canvas.scale(.25f, .25f);
						contextView.draw(canvas);
						blurBitmap = Blur.fastblur(getApplicationContext(), blurBitmap, 1);
						frontView.setImageBitmap(blurBitmap);
						frontView.setVisibility(View.VISIBLE);
					} catch (Exception e) {
					}
				}else if(newState == DrawerLayout.STATE_IDLE && !drawerLayout.isDrawerOpen(GravityCompat.START)){
					frontView.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		//绑定底部按钮与页卡
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroupFragmentBinder(getSupportFragmentManager(), R.id.contain) {
			@Override
			public Fragment instanceFragment(int checkedId) {
				switch(checkedId){
				case R.id.bottom_bar_home: return new HomeFragment();
				case R.id.bottom_bar_show_cars: return new ShowCarsFragment();
				case R.id.bottom_bar_find_cars: return new FindCarsAssistorFragment();
				case R.id.bottom_bar_brand: return new BrandFragment();
				case R.id.bottom_bar_more: return new MoreFragment();
				}
				return null;
			}
			@Override
			public void onChecked(int checkedId, Fragment fragment) {
				super.onChecked(checkedId, fragment);
				if(checkedId==R.id.bottom_bar_show_cars){
					//首次进入选车型的提示
					if(!MyApp.hasKeyOnce("tip_show_cars")){
						final View tip = View.inflate(MainActivity.this, R.layout.tip_choose_model, null);
						tip.setPadding(0 , 0, 0, radioGroup.getHeight());
						addContentView(tip, new FrameLayout.LayoutParams(-1, -1));
						tip.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								((ViewGroup)tip.getParent()).removeView(tip);
							}
						});
					}
				}
			}
		});
		((CompoundButton)radioGroup.findViewById(R.id.bottom_bar_home)).setChecked(true);
		
		//每次进入App显示右滑的提示动画
		final View slibIndicator = findViewById(R.id.main_indicator);
		List<AssetFrame> animFrames = FrameFactory.createFramesFromAsset(this, "arrow_anim/anim", 30);
		List<AssetFrame> dismissFrames = FrameFactory.createFramesFromAsset(this, "arrow_anim/dismiss", 30);
		ArrayList<Frame> frames = new ArrayList<Frame>();
		frames.addAll(animFrames);
		frames.addAll(animFrames);
		frames.addAll(animFrames);
		frames.addAll(dismissFrames);
		FrameAnimation frameAnimation = new FrameAnimation(slibIndicator, frames);
		frameAnimation.setFrameAnimListener(new FrameAnimListener() {
			@Override
			public void onStart(FrameAnimation animation) {
			}
			@Override
			public void onPlaying(FrameAnimation animation, int frameIndex) {
			}
			@Override
			public void onFinish(FrameAnimation animation) {
				((ViewGroup)slibIndicator.getParent()).removeView(slibIndicator);
			}
		});
		frameAnimation.start();
		
		//首次进入操作提示
		if(!MyApp.hasKeyOnce("tip_main")){
			final View tip = View.inflate(this, R.layout.tip_main, null);
			addContentView(tip, new FrameLayout.LayoutParams(-1, -1));
			tip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((ViewGroup)tip.getParent()).removeView(tip);
				}
			});
		}
	}
	
	private void animDrawableRight(final TextView textView){
		textView.getCompoundDrawables()[2].setAlpha(255);
		
		final float mFromAlpha=1;
		final float mToAlpha=0;
		AlphaAnimation alphaAnimation = new AlphaAnimation(mFromAlpha, mToAlpha){
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
		        float alpha = (mFromAlpha + ((mToAlpha - mFromAlpha) * interpolatedTime));
				textView.getCompoundDrawables()[2].setAlpha((int) (alpha * 255));
			}
		};
		alphaAnimation.setDuration(500);
		alphaAnimation.setStartOffset(500);
		
		textView.startAnimation(alphaAnimation);
	}
}
