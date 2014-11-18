package com.fax.faw_vw.more;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.CarInfoList;
import com.fax.faw_vw.model.Illegal;
import com.fax.faw_vw.model.IllegalInfo;
import com.fax.faw_vw.model.CarInfoList.Province;
import com.fax.faw_vw.model.CityInfo;
import com.fax.faw_vw.views.FirstHideSpinnerAdapter;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.HttpAsyncTask;
import com.fax.utils.task.ResultAsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**TODO 违章查询首页 */
public class QueryIllegalIndexFragment extends MyFragment {
	Spinner carSpinner;
	CarInfoList carInfoList;
	private FirstHideSpinnerAdapter emptyAdapter = new FirstHideSpinnerAdapter(new String[]{null}){
		@Override
		public TextView getView(int position, View convertView, ViewGroup parent) {
			TextView tv = super.getView(position, convertView, parent);
//			tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_arrow_down, 0);
			return tv;
		}
	};
	private void initCarSpinner(View view) {
		try {
			String info=(context.getSharedPreferences("QueryIllegalHistory", Context.MODE_PRIVATE).getAll().values()).toString();
			String json= "{'cmd':"+info+"}";
			carInfoList = new Gson().fromJson(json, CarInfoList.class);
			carSpinner = (Spinner) view.findViewById(R.id.more_query_illegalinfo_spinner);
			carSpinner.setAdapter(new FirstHideSpinnerAdapter(carInfoList.getData()){
				@Override
				public TextView getView(int position, View convertView, ViewGroup parent) {
					TextView tv = super.getView(position, convertView, parent);
//					tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_arrow_down, 0);
					tv.setTextColor(getResources().getColor(R.color.white));    
                    tv.setTextSize(18.0f);    
					return tv;
				}
			});
			carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					final Province province = (Province)carSpinner.getItemAtPosition(position);
					if(province == null) return;
					new ResultAsyncTask<IllegalInfo>(context) {
						@SuppressWarnings("deprecation")
						@Override
						protected IllegalInfo doInBackground(Void... params) {
							String IllegalUrl="http://m3.mgogo.com/vwapp/car.php";
							String url ="http://faw-vw.allyes.com/index.php?g=api&m=apicache&a=getdata&url=";
							ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
							pairs.add(new BasicNameValuePair("plateNumber", province.getPlateNumber()));
							pairs.add(new BasicNameValuePair("engineNumber", province.getEngineNumber()));
							pairs.add(new BasicNameValuePair("vehicleIdNumber", province.getVehicleIdNumber()));
							pairs.add(new BasicNameValuePair("cityName",province.getCityName()));
							if (pairs!=null&&pairs.size()>0) {
					    		try {
					    			if(IllegalUrl.contains("?")){
					    				url=url+URLEncoder.encode(IllegalUrl+"&"+URLEncodedUtils.format(pairs, "UTF-8"));
					    			}else{
					    			    url=url+URLEncoder.encode(IllegalUrl+"?"+URLEncodedUtils.format(pairs, "UTF-8"));
					    			}
					    		} catch (Exception e) {
					    			e.printStackTrace();
					    		}
					    	}
							String json=HttpUtils.reqForPost(url);
							try {
								return new Gson().fromJson((new Gson().fromJson(json, Illegal.class)).getBody(), IllegalInfo.class);
							} catch (Exception e) {
							}
							return null;
						}
						@Override
						protected void onPostExecute(IllegalInfo illegal) {
							if(illegal.getResult().getLists().size()!=0){
//	         					Log.e("lib", ""+illegal.getResult().getLists().size());
								QueryIllegalInfoFragment queryIllegalInfoFragment=new  QueryIllegalInfoFragment();
								queryIllegalInfoFragment.onActivityResult(1, Activity.RESULT_OK, MyApp.createIntent(illegal));
								addFragment(queryIllegalInfoFragment);
							}else Toast.makeText(context, "暂无违章记录", Toast.LENGTH_SHORT).show();
							super.onPostExecute(illegal);
						}
						@Override
						protected void onPostExecuteSuc(IllegalInfo result) {
						}
					}.setProgressDialog().execute();
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
			
		} catch (Exception e) {
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.more_query_illegal_index, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
				.setTitle("违章查询").setContentView(view);
		  initCarSpinner(view);
		  view.findViewById(R.id.add_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						QueryIllegalFragment queryIllegalFragment=new  QueryIllegalFragment();
						addFragment(queryIllegalFragment);
					}
		   });
		//TODO 数据绑定、提交
		return topBar;
	}
}
