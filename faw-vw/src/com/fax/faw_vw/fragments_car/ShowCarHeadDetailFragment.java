package com.fax.faw_vw.fragments_car;

import java.util.List;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.model.ShowCarItemRes;
import com.fax.faw_vw.model.ShowCarItemRes.ShowCarDetailHead;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.view.pager.PointIndicator;
import com.fax.utils.view.pager.SamePagerAdapter;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by linfaxin on 2014/7/29 029.
 * Email: linlinfaxin@163.com
 */
public class ShowCarHeadDetailFragment extends MyFragment{
	View.OnClickListener backClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			backStack();
		}
	};
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(view != null){//防止Fragment在add的时候没有背景色叠加显示出错和touch事件传递底部
			view.setBackgroundResource(android.R.color.transparent);
			view.setOnClickListener(backClick);
		}
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.car_detail_head_detail, container, false);
        final ShowCarDetailHead head = (ShowCarDetailHead) getArguments().getSerializable(ShowCarDetailHead.class.getName());
        final ShowCarItemRes.CarItemChild carItemChild = getSerializableExtra(ShowCarItemRes.CarItemChild.class);
        final ShowCarItem showCarItem = getSerializableExtra(ShowCarItem.class);
        
        view.postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
				String modelName = carItemChild==null?showCarItem.getModel_en():carItemChild.getNameEn();
		        List<AssetFrame> frames = FrameFactory.createFramesFromAsset(context, head.getAssetDir(modelName), -1);
		        
		        viewPager.setAdapter(new SamePagerAdapter<AssetFrame>(frames) {
					@Override
					public View getView(AssetFrame t, int position, View convertView) {
						if(convertView == null){
							convertView = new ImageView(context);
							int padding = (int) MyApp.convertToDp(20);
							convertView.setPadding(padding, 0, padding, 0);
							convertView.setOnClickListener(backClick);
						}
						((ImageView)convertView).setImageDrawable(t.decodeDrawable(context));
						return convertView;
					}
					@Override
					protected void onItemDestroyed(View view, AssetFrame t) {
						super.onItemDestroyed(view, t);
						t.recycle();
					}
				});
		        
		        PointIndicator pointIndicator = (PointIndicator) view.findViewById(R.id.point_indicator);
		        pointIndicator.bindViewPager(viewPager);
		        viewPager.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha_small_to_normal));
		        
		        View handImg = view.findViewById(R.id.tip_hand);
		        handImg.setVisibility(View.VISIBLE);
		        handImg.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_out));
			}
		}, 500);
        return view;
    }
}
