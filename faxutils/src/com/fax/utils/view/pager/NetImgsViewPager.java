package com.fax.utils.view.pager;

import java.util.ArrayList;

import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.bitmap.ProgressImageView;
import com.fax.utils.bitmap.RecycleImageView;
import com.fax.utils.view.photoview.PhotoView;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**ViewPager的网络画廊组件，带进度的展示 */
public class NetImgsViewPager extends ViewPager {
	public NetImgsViewPager(Context context) {
		super(context);
	}
	public NetImgsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	boolean isImageZoomAble ;
	/**设置是否允许手势缩放 */
	public void setImageZoomAble(boolean isImageZoomAble) {
		this.isImageZoomAble = isImageZoomAble;
	}
	ScaleType imageScaleType;
	public void setImageScaleType(ScaleType scaleType){
		imageScaleType=scaleType;
	}
	public void setImgs(final String... mImgUrls){
		if(mImgUrls==null) return;
		setAdapter(new NetImgViewPagerAdapter(mImgUrls));
	}
	public void setImgs(final ArrayList<String> mImgUrlList){
		if(mImgUrlList==null) return;
		setAdapter(new NetImgViewPagerAdapter(mImgUrlList.toArray(new String[mImgUrlList.size()])));
	}
	class NetImgViewPagerAdapter extends PagerAdapter {
		private String[] mImgUrls;
		public NetImgViewPagerAdapter(String[] mImgUrls) {
			this.mImgUrls = mImgUrls;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);// 删除页卡
            if(object instanceof PhotoView){
                ((PhotoView)object).stopLoadUrl();
            }
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			int progressSize = (int) (60 * getContext().getResources().getDisplayMetrics().density);
			final PhotoView imgView = new PhotoView(getContext());
			imgView.setProgressSize(progressSize);
			
			imgView.loadUrl(mImgUrls[position]);
			if(imageScaleType!=null){
				imgView.setScaleType(imageScaleType);
			}
			if(!isImageZoomAble){
				imgView.setZoomable(false);
			}
			container.addView(imgView, 0);
			if(listener!=null) listener.onInstantiateItem(imgView, position);
			return imgView;
		}
		@Override
		public int getCount() {
			return mImgUrls.length;// 返回页卡的数量
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ignore) {//avoid pointerIndex out of range in some android4.x
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ignore) {//avoid pointerIndex out of range in some android4.x
        }
        return false;
    }
	OnInstantiateItemListener listener;
	public void setOnInstantiateItemListener(OnInstantiateItemListener listener){
		this.listener=listener;
	}
	public interface OnInstantiateItemListener{
		/**
		 * 用这个方法来监听对View初始化
		 * @param imgView 新实例化的对象
		 * @param position 对象的position
		 */
		public void onInstantiateItem(PhotoView imgView, int position);
	}
}
