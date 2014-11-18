package com.fax.faw_vw.more;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.Illegal;
import com.fax.faw_vw.model.IllegalInfo;
import com.fax.faw_vw.model.QueryJavaBean;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.ResultAsyncTask;
import com.google.gson.Gson;

/**TODO 违章查询页面 */
public class QueryIllegalFragment extends MyFragment {

	EditText plateNumber;
	EditText engineNumber;
	EditText vehicleIdNumber;
	EditText cityName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.more_query_illegal, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
				.setTitle("违章查询").setContentView(view);
		plateNumber=(EditText) view.findViewById(R.id.plateNumber);
		engineNumber=(EditText) view.findViewById(R.id.engineNumber);
		vehicleIdNumber=(EditText) view.findViewById(R.id.vehicleIdNumber);
		cityName=(EditText) view.findViewById(R.id.cityName);
		view.findViewById(R.id.commit_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(plateNumber.getText())){
					Toast.makeText(context, "请输入您的车牌号！", 3000).show();
					return;
				}
				if(TextUtils.isEmpty(engineNumber.getText())){
					Toast.makeText(context, "请输入您的发动机号！", 3000).show();
					return;
				}
				if(TextUtils.isEmpty(vehicleIdNumber.getText())){
					Toast.makeText(context, "请输入您的车架号！", 3000).show();
					return;
				}
				if(TextUtils.isEmpty(cityName.getText())){
					Toast.makeText(context, "请输入城市！", 3000).show();
					return;
				}
				QueryJavaBean qj=new QueryJavaBean();
				qj.setPlateNumber(plateNumber.getText().toString());
				qj.setEngineNumber(engineNumber.getText().toString());
				qj.setVehicleIdNumber(vehicleIdNumber.getText().toString());
				qj.setCityName(cityName.getText().toString());
				String illegal = new Gson().toJson(qj);
				SharedPreferences sp= context.getSharedPreferences("QueryIllegalHistory", Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString(String.valueOf(new Date().getTime()),illegal);
				editor.commit();
				new ResultAsyncTask<IllegalInfo>(context) {
					@SuppressWarnings("deprecation")
					@Override
					protected IllegalInfo doInBackground(Void... params) {
						String IllegalUrl="http://m3.mgogo.com/vwapp/car.php";
						String url ="http://faw-vw.allyes.com/index.php?g=api&m=apicache&a=getdata&url=";
								
						ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
						pairs.add(new BasicNameValuePair("plateNumber", plateNumber.getText().toString()));
						pairs.add(new BasicNameValuePair("engineNumber", engineNumber.getText().toString()));
						pairs.add(new BasicNameValuePair("vehicleIdNumber", vehicleIdNumber.getText().toString()));
						pairs.add(new BasicNameValuePair("cityName",cityName.getText().toString()));
						
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
//         					Log.e("lib", ""+illegal.getResult().getLists().size());
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
		});
		//TODO 数据绑定、提交
		return topBar;
	}
	
	public static final String PATH = "/data/data/com.fax.faw_vw/shared_prefs/QueryIllegalHistory.xml";  
	private String print() {  
        StringBuffer buff = new StringBuffer();  
        try {  
            BufferedReader reader = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(PATH)));  
            String str;  
            while ((str = reader.readLine()) != null) {  
                buff.append(str + "/n");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return buff.toString();  
    }  

}
