package com.fax.faw_vw.game;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.ImageView.ScaleType;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.util.SimpleDirectionGesture;
import com.fax.utils.frameAnim.BasicBitmapFrame;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameAnimListener;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;

public class OnlineDriveGameActivity extends Activity {
	FrameLayout coinsLayout;
	VideoView videoView;
	ImageView centerInfo;
	Button controlBtn;
	ImageView carImg;

	Handler handler = new Handler();
	int score = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.online_drive_game);
		coinsLayout = (FrameLayout) findViewById(R.id.online_drive_game_coins_layout);
		videoView = (VideoView) findViewById(R.id.videoView);
		videoView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
		centerInfo = (ImageView) findViewById(R.id.online_drive_game_center_info);
		controlBtn = (Button) findViewById(R.id.online_drive_game_front_control);
		carImg = (ImageView) findViewById(R.id.online_drive_game_car_view);
		
		initVideoView();
		initControlBtn();
		initEatCoin();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPool.release();
	}

	private void initVideoView(){
		String uri = "android.resource://" + getPackageName() + "/" + R.raw.run_final_480p;
		videoView.setVideoPath(uri);
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				startGame();
			}
		});
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				List<? extends Frame> frames = FrameFactory.createFramesFromAsset(OnlineDriveGameActivity.this, "online_drive_game/game_finish", 50);
				((BasicBitmapFrame)frames.get(frames.size()-1)).setDuration(1500);
				showCenterInfoAnim(frames, new FrameAnimListener() {
					public void onStart(FrameAnimation animation) {
					}
					public void onPlaying(FrameAnimation animation, int frameIndex) {
					}
					@Override
					public void onFinish(FrameAnimation animation) {
						//显示结束的View
						final View view = View.inflate(OnlineDriveGameActivity.this, R.layout.online_drive_game_finish, null);
				    	((TextView)view.findViewById(android.R.id.text1)).setTypeface(scoreTv.getTypeface());
						((TextView)view.findViewById(android.R.id.text1)).setText(""+score);//分数
						view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
							}
						});
						view.findViewById(android.R.id.button2).setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								((ViewGroup)view.getParent()).removeView(view);
//								startGame();
								finish();
								startActivity(new Intent(OnlineDriveGameActivity.this, OnlineDriveGameActivity.class));
							}
						});
						addContentView(view, new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
						view.startAnimation(AnimationUtils.loadAnimation(OnlineDriveGameActivity.this, R.anim.fade_in_scale_to_top));
						
					}
				});
				

				//车辆向前冲的动画
				final int endY = - (coinsLayout.getHeight()* 270/640);
				final float endScale = 0.0f;
				AnimationSet set = new AnimationSet(true);
				set.addAnimation(new ScaleAnimation(1, endScale, 1, endScale, 
						Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f));
				set.addAnimation(new TranslateAnimation(0, 0, 0, endY));
				set.setDuration(500);
				set.setFillAfter(true);
				carImg.startAnimation(set);
			}
		});

		videoView.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels;
		videoView.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
		videoView.requestLayout();
	}
	private void startGame(){
		carImg.clearAnimation();
		videoView.seekTo(1);
		List<? extends Frame> frames = FrameFactory.createFramesFromAsset(OnlineDriveGameActivity.this, "online_drive_game/game_start", 30);
		showCenterInfoAnim(frames, new FrameAnimListener() {
			@Override
			public void onStart(FrameAnimation animation) {
			}
			@Override
			public void onPlaying(FrameAnimation animation, int frameIndex) {
			}
			@Override
			public void onFinish(FrameAnimation animation) {
				videoView.start();
				initCoins();
			}
		});
	}
	
	boolean canEatLeft,canEatRight;
	boolean canEatCenter = true;
	private void initControlBtn(){
		final List<? extends Frame> totalFrames = FrameFactory.createFramesFromAsset(this, "online_drive_game/car_golf", 30);
		final List<? extends Frame> centerToLeft = totalFrames.subList(0, 14);
		final List<? extends Frame> leftToCenter = totalFrames.subList(14, 28);
		final List<? extends Frame> centerToRight = totalFrames.subList(28, 41);
		final List<? extends Frame> rightToCenter = totalFrames.subList(41, totalFrames.size());
		
		controlBtn.setOnTouchListener(new SimpleDirectionGesture(controlBtn) {
			List<? extends Frame> lastFrames;
			FrameAnimation lastAnim;
			@Override
			protected void onFling(int direction) {
				if(!videoView.isPlaying()) return;//视频没有在播放不能控制
				
				if(lastAnim==null) lastAnim = new FrameAnimation(carImg);
				else if(!lastAnim.isFinish()) return;
				
				List<? extends Frame> temp = lastFrames;
				
				if(direction==SimpleDirectionGesture.Direction_Left){
					//汽车左滑
					if(lastFrames==null || lastFrames==leftToCenter || lastFrames==rightToCenter){
						lastFrames = centerToLeft;
					}else if(lastFrames == centerToRight){
						lastFrames = rightToCenter;
					}
					
				}else if(direction==SimpleDirectionGesture.Direction_Right){
					//汽车右滑
					if(lastFrames==null || lastFrames==leftToCenter || lastFrames==rightToCenter){
						lastFrames = centerToRight;
					}else if(lastFrames == centerToLeft){
						lastFrames = leftToCenter;
					}
					
				}
				
				if(temp!=lastFrames){//开始动画
					lastAnim.setFrames(lastFrames);
					lastAnim.start();
					lastAnim.setFrameAnimListener(new FrameAnimListener() {
						@Override
						public void onStart(FrameAnimation animation) {
						}
						@Override
						public void onPlaying(FrameAnimation animation, int frameIndex) {
							canEatLeft=canEatCenter=canEatRight=false;
							
							if(lastFrames == centerToLeft){
								if(frameIndex<lastFrames.size()*2/5) canEatCenter = true;
								else if(frameIndex>lastFrames.size()*3/5) canEatLeft = true;
								
							}else if(lastFrames == rightToCenter){
								if(frameIndex<lastFrames.size()*2/5) canEatRight = true;
								else if(frameIndex>lastFrames.size()*3/5) canEatCenter = true;
								
							}else if(lastFrames == centerToRight){
								if(frameIndex<lastFrames.size()*2/5) canEatCenter = true;
								else if(frameIndex>lastFrames.size()*3/5) canEatRight = true;
								
							}else if(lastFrames == leftToCenter){
								if(frameIndex<lastFrames.size()*2/5) canEatLeft = true;
								else if(frameIndex>lastFrames.size()*3/5) canEatCenter = true;
								
							}
						}
						@Override
						public void onFinish(FrameAnimation animation) {
						}
					});
				}
			}
		});
	}
	

	private void showCenterInfoAnim(List<Frame> frames){
		showCenterInfoAnim(frames, null);
	}
	private void showCenterInfoAnim(List<? extends Frame> frames, final FrameAnimListener animListener){
		FrameAnimation fa = new FrameAnimation(centerInfo, frames);
		fa.setFrameAnimListener(new FrameAnimListener() {
			@Override
			public void onStart(FrameAnimation animation) {
				if(animListener!=null) animListener.onStart(animation);
			}
			@Override
			public void onPlaying(FrameAnimation animation, int frameIndex) {
				if(animListener!=null) animListener.onPlaying(animation, frameIndex);
			}
			@Override
			public void onFinish(FrameAnimation animation) {
				centerInfo.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
				if(animListener!=null) animListener.onFinish(animation);
			}
		});
		fa.start();
	}
	
	private void initCoins(){
		handler.removeCallbacksAndMessages(null);
		handler.postDelayed(new Runnable() {
			Random r = new Random();
			int nextRoad;
			int delay;
			@Override
			public void run() {
				if(videoView.isPlaying() && videoView.getDuration()-videoView.getCurrentPosition()>8000){
					addCoin(nextRoad);
					
					delay = 2000;
					if(r.nextInt(3)==0) nextRoad = r.nextInt(3)-1;
					else delay = 200;
					handler.postDelayed(this, delay);
				}
			}
		}, 3000);
	}
	/**
	 * 加一个金币
	 * @param roadIndex -1,0,1 左中右
	 */
	private void addCoin(int roadIndex){
		final ImageView coinImg = new ImageView(this);
		coinImg.setScaleType(ScaleType.FIT_CENTER);
		Bitmap coinBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.online_drive_game_coin);
		coinImg.setImageBitmap(coinBitmap);
		final int defaultWidth = coinBitmap.getWidth();
		final int defaultHeight = coinBitmap.getHeight();
		
		final int startY = coinsLayout.getHeight()* 270/640 - defaultHeight/2;
		final int endY = coinsLayout.getHeight();
		final int startX = 0;
		final int endX = startX + coinsLayout.getWidth()* 300/1200 * roadIndex;
		final float startScale = 0.0f;
		final float endScale = 2.0f;
		
		
		
		final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(0, 0, Gravity.CENTER_HORIZONTAL);
		coinsLayout.addView(coinImg, params);
		Animation anim = new Animation(){
			@Override
			public boolean willChangeTransformationMatrix() {
				return true;
			}
			@Override
			public boolean willChangeBounds() {
				return true;
			}
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				float scale = interpolatedTime * (endScale - startScale) + startScale;
				int x = (int) (interpolatedTime * (endX - startX) + startX);
				int y = (int) (interpolatedTime * (endY - startY) + startY);
				params.width = (int) (scale * defaultWidth);
				params.height = (int) (scale * defaultHeight);
				if(endX<0) params.rightMargin = -x;
				else if(endX>0) params.leftMargin = x;
				params.topMargin = y;
				coinImg.requestLayout();
				
				float underTop = (params.topMargin + params.height - carImg.getTop()) * 1f / carImg.getHeight();
				// 检查被吃的可能了
				if ((underTop > .35f && x < startX && canEatLeft)
						|| (underTop > .25f && x == startX && canEatCenter)
						|| (underTop > .35f && x > startX && canEatRight)) {
					coinImg.clearAnimation();
					showEatCoin(coinImg);
				}
				if(interpolatedTime>=1){
					removeCoinImgDelay(coinImg);
				}
			}
		};
		
		Interpolator interpolator = new AccelerateInterpolator(3f);
		anim.setInterpolator(interpolator);
		anim.setDuration(4000);
		coinImg.startAnimation(anim);
	}
	
	TextView scoreTv;
	SoundPool soundPool;
	int eatCoinSoundId;
	private void initEatCoin(){
		scoreTv = (TextView) findViewById(R.id.online_drive_game_score);
    	Typeface typeFace =Typeface.createFromAsset(getAssets(),"online_drive_game/font.ttf");
    	scoreTv.setTypeface(typeFace);
    	
    	soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
    	eatCoinSoundId = soundPool.load(this, R.raw.eat_coin, 100);
	}
	private void showEatCoin(final ImageView coinImg){
		soundPool.play(eatCoinSoundId, 1, 1, 100, 0, 1);
		score ++;
		scoreTv.setText(score+"");
		final ImageView hitAnim = new ImageView(this);
		hitAnim.setScaleType(ScaleType.FIT_CENTER);
		FrameLayout.LayoutParams iconLayoutParams = (LayoutParams) coinImg.getLayoutParams();
		FrameLayout.LayoutParams hitAnimLayout = new FrameLayout.LayoutParams(iconLayoutParams.width*3, iconLayoutParams.height*3, Gravity.CENTER_HORIZONTAL);
		hitAnimLayout.topMargin = iconLayoutParams.topMargin;
		if(iconLayoutParams.leftMargin!=0) hitAnimLayout.leftMargin = iconLayoutParams.leftMargin+iconLayoutParams.width/2;
		if(iconLayoutParams.rightMargin!=0) hitAnimLayout.rightMargin = iconLayoutParams.rightMargin+iconLayoutParams.width/2;
		coinsLayout.addView(hitAnim, hitAnimLayout);
		FrameAnimation fa = new FrameAnimation(hitAnim, FrameFactory.createFramesFromAsset(this, "online_drive_game/eat_coin", 40));
		fa.setFrameAnimListener(new FrameAnimListener() {
			@Override
			public void onStart(FrameAnimation animation) {
			}
			@Override
			public void onPlaying(FrameAnimation animation, int frameIndex) {
			}
			@Override
			public void onFinish(FrameAnimation animation) {
				coinsLayout.removeView(hitAnim);
			}
		});
		fa.start();
		
		Animation flip = AnimationUtils.loadAnimation(this, R.anim.fade_out_flip_to_top);
		coinImg.startAnimation(flip);
		flip.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				removeCoinImgDelay(coinImg);
			}
		});
	}
	private void removeCoinImgDelay(final ImageView coinImg){
		coinsLayout.postDelayed(new Runnable() {
			public void run() {
				coinsLayout.removeView(coinImg);
			}
		}, 10);
	}
}
