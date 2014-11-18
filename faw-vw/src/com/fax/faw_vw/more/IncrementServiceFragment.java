package com.fax.faw_vw.more;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
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
	}
    public final static int Request_SwitchCity = 1;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK && requestCode == Request_SwitchCity){
			String city = data.getStringExtra(HomeCitySwitchFragment.Extra_City);
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
						weatherinfo_text.setTextMulti(getWeatherinfo(result.getResult().getCurrentCity(), result.getDate(), result.getResult().getWeather_data().getTemperature(), result.getResult().getWeather_data().getWeather()));
						wind_text.setText(result.getResult().getWeather_data().getWind());
						int pm25 = responseResult.getPm25();
						String pmState = pm25 < 50 ? "优" : pm25 > 100 ? "差" : "良";
						weather_PM.setText(pmState);
						//TODD  PM2.5小贴士
						weather_PM_tip.setTextMulti(" ====");
						//TODD 显示背景及显示图标
						
						
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
		
		return "//S50"+temprature+"//S20"+weather+"\n"+city+"//"+" "+HanziToPinyin.getFullPinYin(city)+"\n"+date;
	}
}
