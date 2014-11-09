package com.fax.faw_vw.fragments_main;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.FragmentContainLandscape;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.findcar.FinancialServiceProductFragment;
import com.fax.faw_vw.fragment_360.Show360FrameFragment;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_car.CarDetailFragment;
import com.fax.faw_vw.fragments_car.CarDownloadFragment;
import com.fax.faw_vw.fragments_car.MarketFragment;
import com.fax.faw_vw.fragments_car.NewsFragment;
import com.fax.faw_vw.fragments_car.OnlineOrderCarFragment;
import com.fax.faw_vw.game.OnlineDriveGamePreStartFrag;
import com.fax.faw_vw.model.ImageResPagePair;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.utils.view.pager.NetImgsViewPager;
import com.fax.utils.view.pager.PointIndicator;
import com.fax.utils.view.pager.SamePagerAdapter;
import com.fax.utils.view.photoview.PhotoView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
/**首页 页卡 */
public class HomeFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_home, container, false);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
		viewPager.setPageMargin((int) MyApp.convertToDp(4));
		viewPager.getLayoutParams().height = getResources().getDisplayMetrics().widthPixels * 300 / 612;
		ImageResPagePair[] pagePairs = new ImageResPagePair[]{
				new ImageResPagePair(R.drawable.main_home_pager_img_0, MyApp.createFragment(FinancialServiceProductFragment.class)),
				new ImageResPagePair(R.drawable.main_home_pager_img_1, MyApp.createFragment(OnlineDriveGamePreStartFrag.class), true),
				new ImageResPagePair(R.drawable.main_home_pager_img_2, MyApp.createFragment(NewsFragment.class)),
				new ImageResPagePair(R.drawable.main_home_pager_img_3, MyApp.createFragment(ShowCarsFragment.class, Show360FrameFragment.class))
		};
		viewPager.setAdapter(new SamePagerAdapter<ImageResPagePair>(pagePairs) {
			@Override
			public View getView(final ImageResPagePair t, int position, View convertView) {
				if(convertView==null){
					convertView = new ImageView(context);
					((ImageView)convertView).setScaleType(ScaleType.CENTER_CROP);
				}
				((ImageView)convertView).setImageResource(t.getImgResId());
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(t.getFragment()!=null){
							if(t.isLandscape()){
								FragmentContainLandscape.start(getActivity(), t.getFragment());
								
							}else FragmentContain.start(getActivity(), t.getFragment());
							
						}
//						else if(t.getActivityClass()!=null){
//							startActivity(new Intent(context, t.getActivityClass()));
//						}
					}
				});
				return convertView;
			}
		});
		PointIndicator pointIndicator = (PointIndicator) view.findViewById(R.id.point_indicator);
		pointIndicator.bindViewPager(viewPager);
		
		ViewPager viewPager360 = (ViewPager) view.findViewById(R.id.home_show_360);
		viewPager360.getLayoutParams().height = getResources().getDisplayMetrics().widthPixels * 185 / 640;
		viewPager360.setAdapter(new SamePagerAdapter<Void>() {
			ShowCarItem[] carItems = new ShowCarItem[]{
					ShowCarItem.SHOW_CAR_ITEM_GOLF,
					ShowCarItem.SHOW_CAR_ITEM_JETTA,
					ShowCarItem.SHOW_CAR_ITEM_BORA,
					ShowCarItem.SHOW_CAR_ITEM_MAGOTAN,
					ShowCarItem.SHOW_CAR_ITEM_SAGITAR,
					ShowCarItem.SHOW_CAR_ITEM_CC,
			};
			int[] resIds = new int[]{
					R.drawable.main_home_360_img_0,
					R.drawable.main_home_360_img_1, R.drawable.main_home_360_img_2, R.drawable.main_home_360_img_3, 
					R.drawable.main_home_360_img_4, R.drawable.main_home_360_img_5
			};
			@Override
			public View getView(Void v, int position, View convertView) {
				if(convertView==null){
					convertView = new ImageView(context);
					((ImageView)convertView).setScaleType(ScaleType.FIT_XY);
				}
				final int realIndex = position % resIds.length;
				((ImageView)convertView).setImageResource(resIds[realIndex]);
				convertView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						MyApp.look360Car(context, carItems[realIndex]);
					}
				});
				return convertView;
			}
			@Override
			public int getCount() {
				return Integer.MAX_VALUE;
			}
			@Override
			protected Void getItemAtPosition(int position) {
				return null;
			}
		});
		viewPager360.setCurrentItem(Integer.MAX_VALUE/6/2*6);
		
		
		view.findViewById(R.id.home_order_drive_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentContain.start(getActivity(), BookDriveFragment.class);
			}
		});
		view.findViewById(R.id.home_discount_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentContain.start(getActivity(), MarketFragment.class);
			}
		});
		view.findViewById(R.id.home_store_search).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentContain.start(getActivity(), SearchDealerFragment.class);
			}
		});
		
		//TODO 天气的获取的展示，使用GsonAycnTask或者HttpAycnTask来完成
		
		return view;
	}
}
