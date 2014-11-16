package com.fax.faw_vw.fragments_main;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
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
import com.fax.faw_vw.model.WeatherResponse;
import com.fax.faw_vw.util.LocManager;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.ResultAsyncTask;
import com.fax.utils.view.pager.NetImgsViewPager;
import com.fax.utils.view.pager.PointIndicator;
import com.fax.utils.view.pager.SamePagerAdapter;
import com.fax.utils.view.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
/**首页 页卡 */

public class HomeFragment extends MyFragment{
	private Boolean b=true;
	private TextView mcity_name,mdate,mtemperature,mchangecity,mPM;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.main_home, container, false);
		mcity_name=(TextView) view.findViewById(R.id.cityname_textview);
		mdate=(TextView) view.findViewById(R.id.date_textview);
		mtemperature=(TextView) view.findViewById(R.id.temperature_textview);
		mchangecity=(TextView) view.findViewById(R.id.change_city_textview);
		mPM=(TextView) view.findViewById(R.id.PM_textview);
		
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
		
		final ViewPager viewPager360 = (ViewPager) view.findViewById(R.id.home_show_360);
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
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				viewPager360.setCurrentItem(viewPager360.getCurrentItem()+1);
				handler.postDelayed(this, 3000);
			}
		}, 2000);
		
		
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
		
	
		
		mchangecity.setOnClickListener(new View.OnClickListener() {
			//切换城市点击事件
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("fax", "切换城市");
			}
		});
		//TODO 天气的获取的展示，使用GsonAycnTask或者HttpAycnTask来完成
		LocManager.reqLoc(context, new LocManager.LocationListener() {
			@Override
			public void onFindLocation(AMapLocation aMapLocation) {
				Log.w("fax", "city:"+aMapLocation.getCityCode()+"location:"+aMapLocation);
				if(b==true){
					showWeather(aMapLocation.getCity(), context);
					b=false;
					
				}
			}
		});

		return view;
	}
	public void showWeather(final String cityname,Context context){
		
		new ResultAsyncTask<WeatherResponse>(context) {

			@SuppressWarnings("deprecation")
			@Override
			protected void onPostExecuteSuc(WeatherResponse result) {
				// TODO Auto-generated method stub
				if(result.getError()==0){
					Log.w("fax",result.getResults().get(0).getCurrentCity());
					if(result.getResults().size()>=0){
						mcity_name.setText(result.getResults().get(0).getCurrentCity());
						mdate.setText(result.getDate());
						mPM.setText(result.getResults().get(0).getPm25());
						mtemperature.setText(result.getResults().get(0).getWeather_data().get(0).getTemperature());
						SimpleDateFormat sdf = new SimpleDateFormat("HH");
						int hour= Integer.parseInt(sdf.format(new Date()));
						BitmapDrawable drawableright;
						Bitmap bitmap;
						//判断白天晚上
						if((hour>=7)&&(hour<=19)==true){
							bitmap=BitmapManager.getBitmap(result.getResults().get(0).getWeather_data().get(0).getDayPictureUrl());
						}else{
							
						}
					
					}
				}
			}
			@Override
			protected WeatherResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String url="http://api.map.baidu.com/telematics/v3/weather";
				 ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("location", cityname));
				pairs.add(new BasicNameValuePair("output", "json"));
				pairs.add(new BasicNameValuePair("ak", "zrozpc3abEsmGCwQRNIXbmk8"));
				String json=HttpUtils.reqForGet(url,pairs);
				WeatherResponse response;
				try {
					response = new Gson().fromJson(json, WeatherResponse.class);
					return response;
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}.setProgressDialog().execute();
		
	}
	
}
