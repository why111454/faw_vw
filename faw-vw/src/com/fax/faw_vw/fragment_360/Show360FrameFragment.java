package com.fax.faw_vw.fragment_360;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.FragmentContainLandscape;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_360.PackedFileLoader.FrameInfo;
import com.fax.faw_vw.fragment_360.PackedFileLoader.FrameInfo.KeyPoint;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.util.OpenFileUtil;
import com.fax.faw_vw.util.SimpleDirectionGesture;
import com.fax.utils.frameAnim.FrameAnimListener;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.task.ResultAsyncTask;

public class Show360FrameFragment extends MyFragment {
	PackedFileLoader fileLoader;
	ArrayList<FrameInfo> frameInfos;
	ImageView redPoint;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.show360_key_frames, container, false);
		final ImageView imageView = (ImageView) view.findViewById(android.R.id.background);
		final FrameLayout frontLay = (FrameLayout) view.findViewById(R.id.show360_key_frames_front_point);
		final ShowCarItem carItem = getSerializableExtra(ShowCarItem.class);

		view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		
		fileLoader = PackedFileLoader.getInstance(context, carItem);
		if(fileLoader==null){
			MyApp.look360Car(context, carItem);
			return view;
		}
		if(!(context instanceof FragmentContainLandscape)){//如果不是横向的屏幕，那么强制
			Fragment fragment = MyApp.createFragment(Show360FrameFragment.class, carItem);
			FragmentContainLandscape.start((Activity) context, fragment);
			getActivity().finish();
			return view;
		}
		
		view.setOnTouchListener(new SimpleDirectionGesture(view){
            @Override
            protected void onFling(int direction) {
            	if(direction == SimpleDirectionGesture.Direction_Left){
            		playNextFrames();
            	}else if(direction == SimpleDirectionGesture.Direction_Right){
            		playLastFrames();
            	}
            }
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(infoView != null && event.getAction() == MotionEvent.ACTION_UP){
					frontLay.removeView(infoView);
				}
				return super.onTouch(v, event);
			}
        });
		frameInfos = fileLoader.getFrameInfoList();
		imageView.setImageDrawable(frameInfos.get(0).frames.get(0).decodeDrawable(context));
		view.findViewById(R.id.detail_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//查看详细情况
				if(preShowIndex == 2){
					//查看全景
					FragmentContain.start(getActivity(), MyApp.createFragment(PanoFragment.class, getArguments()));
				}
				else addFragment(MyApp.createFragment(Touch360Fragment.class, getArguments()));
				
			}
		});
		redPoint = (ImageView) view.findViewById(android.R.id.icon);
		maxProgress = (frameInfos.get(frameInfos.size()-1).getKeyFrame() - 1);
		LinearLayout linear = (LinearLayout) view.findViewById(android.R.id.summary);
		for(int i=0,size=frameInfos.size(); i<size; i++){
			TextView tv = new TextView(context);
			tv.setText(frameInfos.get(i).title);
			tv.setTextSize(12);
			if(i==0) tv.setGravity(Gravity.LEFT);
			else if(i==size-1) tv.setGravity(Gravity.RIGHT);
			else tv.setGravity(Gravity.CENTER);
			linear.addView(tv, new LinearLayout.LayoutParams(0, -2, 1));
		}
		
		frameAnimation = new FrameAnimation(imageView);
		frameAnimation.setFrameAnimListener(new FrameAnimListener() {
			@Override
			public void onPlaying(FrameAnimation frameAnimation, int frameIndex) {
				setProgress(frameInfos.get(showingIndex).getKeyFrame() + frameIndex );
			}
			@Override
			public void onFinish(FrameAnimation frameAnimation) {
				showRedPointToFront(frontLay, imageView, frameInfos.get(preShowIndex));
			}
			@Override
			public void onStart(FrameAnimation animation) {
				frontLay.removeAllViews();
			}
		});
		
		view.postDelayed(new Runnable() {
			@Override
			public void run() {
				showRedPointToFront(frontLay, imageView, frameInfos.get(preShowIndex));
			}
		}, 100);
		return view;
	}
	
	int maxProgress;
	private void setProgress(int progress){
		if(progress>maxProgress) progress = maxProgress;
		int left = progress * (((View)redPoint.getParent()).getWidth()-redPoint.getWidth()) / maxProgress ;
		((MarginLayoutParams)redPoint.getLayoutParams()).leftMargin = left;
		redPoint.requestLayout();
	}
	
	int showingIndex;
	int preShowIndex;//在帧动画开头位置
	FrameAnimation frameAnimation;
	private void playNextFrames(){
		if(frameAnimation.isFinish()){
			if( preShowIndex < frameInfos.size()-1){
				showingIndex = preShowIndex;
				preShowIndex++;
				
				frameAnimation.setFrames(frameInfos.get(showingIndex).frames);
				frameAnimation.start();
			}
		}
	}
	
	private void playLastFrames(){
		if(frameAnimation.isFinish()){
			if( preShowIndex > 0 ){
				preShowIndex--;
				showingIndex = preShowIndex;
				
				frameAnimation.setFrames(frameInfos.get(showingIndex).frames);
				frameAnimation.reverseFrames();
				frameAnimation.start();
			}
		}
	}

	private void showRedPointToFront(FrameLayout front, ImageView imageView, FrameInfo frameInfo){
		int animDelay = 0;
		for(KeyPoint keyPoint : frameInfo.getKeyPoints()){
			View btn = addRedPointToFront(front, imageView, keyPoint);
			Animation anim = AnimationUtils.loadAnimation(context, R.anim.small_to_normal);
			anim.setStartOffset(animDelay);
			btn.startAnimation(anim);
			animDelay+=200;
		}
	}
	ImageView infoView;
	private View addRedPointToFront(final FrameLayout front,final ImageView imageView,final KeyPoint keyPoint){
		ImageButton btn = new ImageButton(context);
		btn.setImageResource(R.drawable.common_ic_red_point);
		btn.setBackgroundResource(R.drawable.common_btn_in_white);
		
		if(imageView.getDrawable()==null) return btn;
		final int size = (int) MyApp.convertToDp(20);
		final int x = 2 * keyPoint.getHot_x() * imageView.getWidth() / imageView.getDrawable().getIntrinsicWidth() - size/2;
		final int y = 2 * keyPoint.getHot_y() * imageView.getHeight() / imageView.getDrawable().getIntrinsicHeight() - size/2;
		
		FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(size, size, Gravity.LEFT|Gravity.TOP);
		param.topMargin = y;
		param.leftMargin = x;
		front.addView(btn, param);
		
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(infoView != null) front.removeView(infoView);
				infoView = new ImageView(context);
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(fileLoader.readKeyPointImageFile(keyPoint));
					infoView.setImageBitmap(bitmap);
					FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(-2, -2, Gravity.LEFT|Gravity.TOP);
					int topMargin = 0;
					if(keyPoint.getImage_y()!=0) topMargin = 2 * keyPoint.getImage_y() * imageView.getHeight() / imageView.getDrawable().getIntrinsicHeight() + size/2;
					else topMargin = (int) (y - bitmap.getHeight() * 6 / 10 );
					param.topMargin = topMargin;
					param.leftMargin = x + size;

					front.addView(infoView, param);
					infoView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_to_left));
					infoView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if(infoView != null) front.removeView(infoView);
							if("mp4".equals(keyPoint.type)){
								final File mp4File = new File(context.getExternalCacheDir(), keyPoint.getFilename());
								if(mp4File.exists()){
									startActivity(OpenFileUtil.getOpenIntent(mp4File.getPath()));
								}else{
									new ResultAsyncTask<Boolean>(context) {
										@Override
										protected void onPostExecuteSuc(Boolean result) {
											startActivity(OpenFileUtil.getOpenIntent(mp4File.getPath()));
										}
										@Override
										protected void onPostExecuteFail(Boolean result) {
											mp4File.delete();
										}
										@Override
										protected Boolean doInBackground(Void... params) {
											try {
												IOUtils.copy(fileLoader.readKeyPointMp4File(keyPoint), new FileOutputStream(mp4File));
												return true;
											} catch (Exception e) {
												e.printStackTrace();
											}
											return false;
										}
									}.setProgressDialog().execute();
								}
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return btn;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if(fileLoader!=null) fileLoader.recycle();
	}
}




