package com.fax.faw_vw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.FrameAnimListener;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.view.pager.PointIndicator;
import com.fax.utils.view.pager.ViewsPagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<AssetFrame> frames = FrameFactory.createFramesFromAsset(this, "intro/logo_anim", 20);
		if(frames.size()>0 && !MyApp.hasKeyOnce("intro_1115")){
			ImageView logoAnim = new ImageView(this);
			logoAnim.setScaleType(ScaleType.FIT_XY);
			setContentView(logoAnim);
			FrameAnimation fa = new FrameAnimation(logoAnim, frames);
			fa.start();
			fa.setFrameAnimListener(new FrameAnimListener() {
				@Override
				public void onStart(FrameAnimation animation) {
				}
				@Override
				public void onPlaying(FrameAnimation animation, int frameIndex) {
				}
				@Override
				public void onFinish(FrameAnimation animation) {
					setContentView(R.layout.intro_first);
			        final View startBtn = findViewById(R.id.intro_btn_start);
			        startBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(StartActivity.this, MainActivity.class));
						}
					});
			        
					final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
			        final List<AssetFrame> page1 = FrameFactory.createFramesFromAsset(StartActivity.this, "intro/page_1", 20);
			        final List<AssetFrame> page2 = FrameFactory.createFramesFromAsset(StartActivity.this, "intro/page_2", 20);
			        final List<AssetFrame> page3 = FrameFactory.createFramesFromAsset(StartActivity.this, "intro/page_3", 20);
			        final List<AssetFrame> page4 = FrameFactory.createFramesFromAsset(StartActivity.this, "intro/page_4", 20);
			        final List<AssetFrame> page5 = FrameFactory.createFramesFromAsset(StartActivity.this, "intro/page_5", 20);
			        final List<List<AssetFrame>> pages = Arrays.asList(page1, page2, page3, page4, page5);
			        final ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
			        final List<FrameAnimation> fAnimations = new ArrayList<FrameAnimation>();
			        for(List<AssetFrame> list : pages){
			        	ImageView imageView = new ImageView(StartActivity.this);
			        	imageView.setScaleType(ScaleType.FIT_XY);
			        	list.get(0).setDuration(300);//延迟载入动画
			        	FrameAnimation.setFrameToView(imageView, list.get(0));
			        	imageViews.add(imageView);
			        	fAnimations.add(new FrameAnimation(imageView, list));
			        	
			        }
			        viewPager.setAdapter(new ViewsPagerAdapter(imageViews));
			        
			        final Runnable animCurrentAndStopOther = new Runnable() {
						@Override
						public void run() {
							int position = viewPager.getCurrentItem();
							for(int i =0,size = fAnimations.size(); i<size; i++){
								FrameAnimation fa = fAnimations.get(i);
								if(i!=position){
									fa.showPreStartView();
//									fa.seekTo(fa.getAllFrames().size()-1);
								}else{
									fa.start();
								}
							}
						}
					};
					animCurrentAndStopOther.run();
					
			        PointIndicator pointIndicator = (PointIndicator) findViewById(R.id.point_indicator);
			        pointIndicator.setColorNormal(Color.DKGRAY);
			        pointIndicator.setColorChecked(getResources().getColor(R.color.dark_blue));
			        pointIndicator.bindViewPager(viewPager, new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							animCurrentAndStopOther.run();
							if(position==viewPager.getAdapter().getCount()-1){
								startBtn.setVisibility(View.VISIBLE);
								Animation anim = AnimationUtils.loadAnimation(StartActivity.this, R.anim.fade_in);
								anim.setStartOffset(4000);
								anim.setDuration(anim.getDuration()*2);
								startBtn.startAnimation(anim);
							} else{
								startBtn.setVisibility(View.INVISIBLE);
							}
						}
						@Override
						public void onPageScrolled(int arg0, float offset, int arg2) {
						}
						@Override
						public void onPageScrollStateChanged(int state) {
						}
					});
				}
			});
		}else{
			 startActivity(new Intent(StartActivity.this, MainActivity.class));
		}
	}

}
