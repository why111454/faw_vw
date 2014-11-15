package com.fax.faw_vw.fragments_car;

import com.fax.faw_vw.FragmentContainLandscape;
import com.fax.faw_vw.MainActivity;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.findcar.FinancialServiceFragment;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.game.OnlineDriveGameActivity;
import com.fax.faw_vw.game.OnlineDriveGamePreStartFrag;
import com.fax.faw_vw.model.ShowCarItemRes;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.util.SimpleDirectionGesture;
import com.fax.faw_vw.views.MyTopBar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CarDetailFragment extends MyFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//首次进入选车型的提示
		if(!MyApp.hasKeyOnce("tip_car_detail")){
			final View tip = View.inflate(context, R.layout.tip_car_detail, null);
			getActivity().addContentView(tip, new FrameLayout.LayoutParams(-1, -1));
			tip.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((ViewGroup)tip.getParent()).removeView(tip);
				}
			});
		}
	}

	ShowCarItem showCarItem;
	ShowCarItemRes.CarItemChild carItemChild;
	ScrollView scrollView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		scrollView = (ScrollView) inflater.inflate(R.layout.showcar_detail, container, false);
		showCarItem = getSerializableExtra(ShowCarItem.class);
		carItemChild = getSerializableExtra(ShowCarItemRes.CarItemChild.class);//选中的一个子项
		ShowCarItemRes.CarItemChild[] resChildren = showCarItem.getDetailRes().getItemChildren();//速腾的子/父页面这个值不为空
		
		ShowCarItemRes res = (carItemChild!=null ? carItemChild.getItemRes() : showCarItem.getDetailRes());
		
		LinearLayout headContain = (LinearLayout) scrollView.findViewById(R.id.showcar_detail_head_contain);
		if(resChildren!=null && carItemChild==null){//有子项的父页面用
			
			headContain.removeAllViews();
			for(int i=0,size=resChildren.length;i<size;i++){
				final ShowCarItemRes.CarItemChild carItemChild = resChildren[i];
				ImageView imgView = new ImageView(context);
				imgView.setAdjustViewBounds(true);
				imgView.setImageResource(carItemChild.getImgResId());
				imgView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						replaceFragment(MyApp.createFragment(CarDetailFragment.class, showCarItem, carItemChild));
					}
				});
				headContain.addView(imgView, new LinearLayout.LayoutParams(0, -1, 1));
			}
			
		}else{//其他正常样式
			fillHeadItem(scrollView.findViewById(R.id.showcar_detail_head_1), res.getHeads()[0]);
			fillHeadItem(scrollView.findViewById(R.id.showcar_detail_head_2), res.getHeads()[1]);
			fillHeadItem(scrollView.findViewById(R.id.showcar_detail_head_3), res.getHeads()[2]);
			fillHeadItem(scrollView.findViewById(R.id.showcar_detail_head_4), res.getHeads()[3]);
			fillHeadItem(scrollView.findViewById(R.id.showcar_detail_head_5), res.getHeads()[4]);
			
			if(res.getHeads()[1]==null && res.getHeads()[2]==null)
				((View)scrollView.findViewById(R.id.showcar_detail_head_2).getParent()).setVisibility(View.GONE);
			if(res.getHeads()[3]==null && res.getHeads()[4]==null)
				((View)scrollView.findViewById(R.id.showcar_detail_head_4).getParent()).setVisibility(View.GONE);
			
			if(carItemChild!=null){//是车型子页
				ImageView backTip = new ImageView(context);
				backTip.setImageResource(R.drawable.showcar_detail_sagitar_back_bt);
				backTip.setScaleType(ScaleType.FIT_XY);
				headContain.addView(backTip, -2, -1);
			}
		}
		
		
		
		
		//360车型
		ImageView round360Image = ((ImageView)scrollView.findViewById(R.id.showcar_detail_360_img));
		if(res.getResId360()>0){
			round360Image.setImageResource(res.getResId360());
			round360Image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					MyApp.look360Car(context, showCarItem);
				}
			});
		}else{
			round360Image.setVisibility(View.GONE);
		}
		
		
		//配置表
		scrollView.findViewById(R.id.showcar_detail_bt0).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(ModelListFragment.newInstance(showCarItem, CarSpecsFragment.class));
			}
		});
		//预约试驾
		scrollView.findViewById(R.id.showcar_detail_bt1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(BookDriveFragment.class, showCarItem));
			}
		});
		//车型对比
		scrollView.findViewById(R.id.showcar_detail_bt2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(CarSpecsCompareFragment.class, showCarItem));
			}
		});
		//在线试驾
		scrollView.findViewById(R.id.showcar_detail_bt3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentContainLandscape.start(getActivity(), OnlineDriveGamePreStartFrag.class, MyApp.createIntent(showCarItem), 0);
			}
		});
		//经销商查询
		scrollView.findViewById(R.id.showcar_detail_bt4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(SearchDealerFragment.class, showCarItem));
			}
		});
		//金融服务
		scrollView.findViewById(R.id.showcar_detail_bt5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(FinancialServiceFragment.class, showCarItem));
			}
		});
		//市场活动
		scrollView.findViewById(R.id.showcar_detail_bt6).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(MarketFragment.class, showCarItem));
			}
		});
		//媒体声音
		scrollView.findViewById(R.id.showcar_detail_bt7).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(NewsFragment.class, showCarItem));
			}
		});
		//报价索取
		scrollView.findViewById(R.id.showcar_detail_bottom_btn1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(RequestPriceFragment.class, showCarItem));
			}
		});
		//下载中心
		scrollView.findViewById(R.id.showcar_detail_bottom_btn2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(CarDownloadFragment.class, showCarItem));
			}
		});
		
		String title=showCarItem.getModel_cn();
		if(carItemChild!=null){//是车型子页
			title = carItemChild.getName().toUpperCase();
		}
		
		return new MyTopBar(getActivity()).setLeftBack()
				.setContentView(scrollView).setTitle(title);
	}
	
	private void fillHeadItem(View headItem, final ShowCarItemRes.ShowCarDetailHead detailHead){
		if(detailHead == null){
			headItem.setVisibility(View.GONE);
		}else{
			((ImageView)headItem.findViewById(android.R.id.background)).setImageResource(detailHead.getResId());
			((TextView)headItem.findViewById(android.R.id.title)).setText(detailHead.getTitle());
			
			headItem.setOnTouchListener(new SimpleDirectionGesture(headItem) {
				
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
					if(distanceX>ViewConfiguration.get(context).getScaledTouchSlop() && distanceX>distanceY)
						scrollView.requestDisallowInterceptTouchEvent(true);
					return super.onScroll(e1, e2, distanceX, distanceY);
				}
				@Override
				public void onFling(int direction) {//返回车型的父页面
					if(carItemChild!=null && direction == SimpleDirectionGesture.Direction_Left){
						replaceFragment(MyApp.createFragment(CarDetailFragment.class, showCarItem));
					}
				}
				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
					addFragment(ft, MyApp.createFragment(ShowCarHeadDetailFragment.class, detailHead, carItemChild, showCarItem));
					ft.commit();
					return super.onSingleTapConfirmed(e);
				}
			});
		}
	}
	
}
