package com.fax.faw_vw.more;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragments_main.HomeCitySwitchFragment;
import com.fax.faw_vw.fragments_main.HomeFragment;
import com.fax.faw_vw.model.HomeTipConfig;
import com.fax.faw_vw.model.OilPriceResponse;
import com.fax.faw_vw.model.WeatherResponse;
import com.fax.faw_vw.util.HanziToPinyin;
import com.fax.faw_vw.util.LocManager;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.faw_vw.views.clickshow.ClickShowTextView;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.http.RequestFactory;
import com.fax.utils.task.GsonAsyncTask;
import com.fax.utils.task.ResultAsyncTask;
import com.fax.utils.view.MultiFormatTextView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**TODO 增值服务页面 */
public class IncrementServiceFragment extends MyFragment {
	private com.fax.utils.view.MultiFormatTextView weatherinfo_text;
	private ImageView weatherinfo_icon;
	private com.fax.faw_vw.views.clickshow.ClickShowTextView changecity_text;
	private TextView wind_text, tip_text,weather_PM,clean_car,love_car_tip,oil_97,oil_93;
	private com.fax.utils.view.MultiFormatTextView weather_PM_tip;
	private RelativeLayout weather_info_bg;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.more_increment_service, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
				.setTitle("增值服务").setContentView(view);
		initView(view);
		changecity_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentContain.start(IncrementServiceFragment.this, HomeCitySwitchFragment.class, Request_SwitchCity);
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
		//TODO 数据绑定、提交
		return topBar;
	}
	public void initView(View view){
		//天气信息
		weatherinfo_text=(MultiFormatTextView) view.findViewById(R.id.more_incrementservice_weather_info);
		//天气图标
		weatherinfo_icon=(ImageView) view.findViewById(R.id.more_incrementservice_weather_icon);
		//切换城市
		changecity_text=(ClickShowTextView) view.findViewById(R.id.more_incrementservice_switch_city);
		//风力
		wind_text=(TextView) view.findViewById(R.id.more_incrementservice_wind);
		//Tips
		tip_text=(TextView) view.findViewById(R.id.more_incrementservice_tip_txt);
		//空气质量
		weather_PM=(TextView) view.findViewById(R.id.more_incrementservice_weather_PM);
		//PM2.5小贴士
		weather_PM_tip=(MultiFormatTextView) view.findViewById(R.id.more_incrementservice_weather_PM_tip);
		//洗车指数
		clean_car=(TextView) view.findViewById(R.id.more_incrementservice_weather_clean_car);
		//洗车指数下tip
		love_car_tip=(TextView) view.findViewById(R.id.more_incrementservic_love_car_tip);
		//97#
		oil_97=(TextView) view.findViewById(R.id.more_incrementservice_oil_97);
		//#93
		oil_93=(TextView) view.findViewById(R.id.more_incrementservice_oil_93);
		//切换城市
		changecity_text=(ClickShowTextView) view.findViewById(R.id.more_incrementservice_switch_city);
		//天气背景
		weather_info_bg=(RelativeLayout) view.findViewById(R.id.more_incrementservice_weather_bg);
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
			@SuppressLint("NewApi")
			@Override
			protected void onPostExecuteSuc(WeatherResponse result) {
				if(result.getError()==0){
					WeatherResponse.Result responseResult = result.getResult();
					View view = getView();
					if(responseResult!=null && view!=null){
						weatherinfo_text.setTextMulti(getWeatherinfo(result.getResult().getCurrentCity(), result.getDate(), result.getResult().getWeather_data().getTemperature(), result.getResult().getWeather_data().getWeather()));
						wind_text.setText(result.getResult().getWeather_data().getWind());
						Bitmap weather_icon=WeatherResHelper.getIcon(context, result.getResult().getWeather_data().getWeather());
						weatherinfo_icon.setImageBitmap(weather_icon);
						Bitmap weather_bg=WeatherResHelper.getBg(context, result.getResult().getWeather_data().getWeather());
						BitmapDrawable background=new BitmapDrawable(getResources(), weather_bg);
						weather_info_bg.setBackground(background);
						if(responseResult.getPm25().equals("")||responseResult.getPm25()==null){
							int pm25=0;
						}else{
							int pm25 = Integer.parseInt(responseResult.getPm25());
							Log.i("fax",pm25+"test");
							if(pm25<50){
								weather_PM.setBackgroundColor(Color.argb(255, 0, 100, 0));
								weather_PM.setText("优");
								weather_PM_tip.setTextMulti(getPmTipString("空气质量令人满意，基本无空气污染，各类人群可正常活动"));
							}else if(pm25<100){
								weather_PM.setBackgroundColor(Color.argb(255,178,133, 0));
								weather_PM.setText("良");
								weather_PM_tip.setTextMulti(getPmTipString("空气质量还不错！出去走走，感受大自然，让心情放松一下"));
							}else if(pm25<150){
								weather_PM.setBackgroundColor(Color.argb(255,254,71, 0));
								weather_PM.setText("轻度污染");
								weather_PM_tip.setTextMulti(getPmTipString("空气质量一般！敏感人群应减少室外活动"));
							}else if(pm25<200){
								weather_PM.setBackgroundColor(Color.argb(255, 178, 45, 0));
								weather_PM.setText("中度污染");
								weather_PM_tip.setTextMulti(getPmTipString("应减少户外活动，外出时佩戴口罩，敏感人群应尽量避免外出"));
							}else if(pm25<300){
								weather_PM.setBackgroundColor(Color.argb(255,105, 0, 0));
								weather_PM.setText("中度污染");
								weather_PM_tip.setTextMulti(getPmTipString("应减少户外活动，外出时佩戴口罩，敏感人群应留在室内"));
							}else{
								weather_PM.setBackgroundColor(Color.argb(255, 102,26, 0));
								weather_PM.setText("严重污染");
								weather_PM_tip.setTextMulti(getPmTipString("儿童、老年人和病人应停留在室内，避免体力消耗，一般人群避免户外活动"));
							}
						}
						
						//显示爱车贴士
						clean_car.setText(result.getResult().getWeather_Index_data().getTitle());
						love_car_tip.setText(result.getResult().getWeather_Index_data().getDes());
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

		//TODO 显示当前油价（用油价的接口）
         ResultAsyncTask<OilPriceResponse> oiltask=new ResultAsyncTask<OilPriceResponse>(context) {
			
			@Override
			protected void onPostExecuteSuc(OilPriceResponse result) {
				// TODO Auto-generated method stub
				if(result.getError_code()==0){
					oil_93.setText("93#"+result.getResult().getData().get(0).getPrice().getE93());
					oil_97.setText("97#"+result.getResult().getData().get(0).getPrice().getE97());
				}
				
			}

			@Override
			protected OilPriceResponse doInBackground(Void... params) {
				// TODO Auto-generated method stub
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

	public String getWeatherinfo(String city,String date,String temprature,String weather){
		//S50-2°//S20晴\n上海// Shanghai\n2014年3月4日
		Log.w("fax",HanziToPinyin.getFullPinYin(city));
		return "//S50"+temprature+"//S20"+weather+"\n"+city+"//"+" "+HanziToPinyin.getFullPinYin(city)+"\n"+date;
	}
	public String getPmTipString(String tipcontent){
		return "PM2.5小贴士\n//S12"+tipcontent;
	}
}
