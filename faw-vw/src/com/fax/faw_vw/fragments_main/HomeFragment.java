package com.fax.faw_vw.fragments_main;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
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
import com.fax.faw_vw.model.HomeTipConfig;
import com.fax.faw_vw.model.ImageResPagePair;
import com.fax.faw_vw.model.OilPriceResponse;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.model.WeatherResponse;
import com.fax.faw_vw.util.LocManager;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.http.RequestFactory;
import com.fax.utils.task.GsonAsyncTask;
import com.fax.utils.task.HttpAsyncTask;
import com.fax.utils.task.ResultAsyncTask;
import com.fax.utils.view.pager.NetImgsViewPager;
import com.fax.utils.view.pager.PointIndicator;
import com.fax.utils.view.pager.SamePagerAdapter;
import com.fax.utils.view.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
	private TextView mcity_name,mdate,mtemperature,mchangecity,mPM;
	private TextView mprice_95,mprice_93;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.main_home, container, false);
		mcity_name=(TextView) view.findViewById(R.id.home_cityname_textview);
		mdate=(TextView) view.findViewById(R.id.home_date_textview);
		mtemperature=(TextView) view.findViewById(R.id.home_temperature_textview);
		mchangecity=(TextView) view.findViewById(R.id.home_change_city_textview);
		mPM=(TextView) view.findViewById(R.id.home_pm_textview);
		mprice_95=(TextView) view.findViewById(R.id.home_price_95);
		mprice_93=(TextView) view.findViewById(R.id.home_price_93);
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
				FragmentContain.start(HomeFragment.this, HomeCitySwitchFragment.class, Request_SwitchCity);
			}
		});
		SharedPreferences mPerferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if(mPerferences.getString("weathercity", null)!=null){
			showWeather(mPerferences.getString("weathercity", null), context, false);
		}else{
		LocManager.reqLoc(context, new LocManager.LocationListener() {
			boolean isShowed = false;
			@Override
			public void onFindLocation(AMapLocation aMapLocation) {
				if(isShowed) return;
				isShowed = true;
				String city = aMapLocation.getCity();
				if(TextUtils.isEmpty(city)) city = "北京";
				showWeather(city, context, false);
			}
		});
		}
		return view;
	}
	public final static int Request_SwitchCity = 1;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == Request_SwitchCity){
			String city = data.getStringExtra(HomeCitySwitchFragment.Extra_City);
			 SharedPreferences mPerferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
			 SharedPreferences.Editor mEditor = mPerferences.edit();
			 mEditor.putString("weathercity",city);
			 mEditor.commit();
			showWeather(city, context, true);
		}
	}

	public void showWeather(final String cityname,Context context, boolean showProgress){
		//显示天气
		String url="http://api.map.baidu.com/telematics/v3/weather";
		ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("location", cityname));
		pairs.add(new BasicNameValuePair("output", "json"));
		pairs.add(new BasicNameValuePair("ak", "zrozpc3abEsmGCwQRNIXbmk8"));
		HttpRequestBase requestBase = RequestFactory.createGet(url, pairs);
		
		ResultAsyncTask<WeatherResponse> task = new GsonAsyncTask<WeatherResponse>(context, requestBase) {
			@Override
			protected void onPostExecuteSuc(WeatherResponse result) {
				if(result.getError()==0){
					WeatherResponse.Result responseResult = result.getResult();
					View view = getView();
					if(responseResult!=null && view!=null){
						mcity_name.setText(responseResult.getCurrentCity());
						mdate.setText(result.getDate());
							int pm25 = responseResult.getPM25();
							mPM.setText(pm25+"");
							String pmState = pm25 < 50 ? "优" : pm25 > 100 ? "差" : "良";
							((TextView)view.findViewById(R.id.home_pm_textview_state)).setText(pmState);
						mtemperature.setText(responseResult.getWeather_data().getTemperature());
						((TextView)view.findViewById(R.id.home_weather_textview)).setText(responseResult.getWeather_data().getWeather());
						int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						//判断白天晚上
						if( hour>=7 && hour<=19 ){
							BitmapManager.bindView(view.findViewById(R.id.home_weather_icon),
									responseResult.getWeather_data().getDayPictureUrl());
						}else{
							BitmapManager.bindView(view.findViewById(R.id.home_weather_icon),
									responseResult.getWeather_data().getNightPictureUrl());
						}
						//显示爱车贴士
						((TextView)view.findViewById(R.id.home_love_car_tip)).setText(HomeTipConfig.getATip(context));
					}
				}
			}
		};
		
		if(showProgress){
			task.setProgressDialog().setToast(false, true);
		}else{
			task.setToast(false);
		}
		task.execute();
         ResultAsyncTask<OilPriceResponse> oiltask=new ResultAsyncTask<OilPriceResponse>(context) {
			@Override
			protected void onPostExecuteSuc(OilPriceResponse result) {
				// TODO Auto-generated method stub
				if(result.getError_code()==0){
					mprice_93.setText("93#"+result.getResult().getData().get(0).getPrice().getE93());
					mprice_95.setText("97#"+result.getResult().getData().get(0).getPrice().getE97());
				}
				
			}

			@Override
			protected OilPriceResponse doInBackground(Void... params) {
				String url="http://m3.mgogo.com/vwapp/oil.php";
				ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("city", cityname));
				String json=HttpUtils.reqForGet(url, pairs);
				try {
					OilPriceResponse oilresponse=new Gson().fromJson(json, OilPriceResponse.class);
					return oilresponse;
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		oiltask.setToast(false);
		oiltask.execute();
	}

	

}
