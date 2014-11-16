package com.fax.faw_vw.more;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.Respon;
import com.fax.faw_vw.model.Response;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.ResultAsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

//意见反馈
public class FeedbackFragment extends MyFragment{
	EditText content_txt;
	EditText contact_txt;
	
	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			 View view = inflater.inflate(R.layout.app_feedback, container, false);
			MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
					.setTitle("意见反馈").setContentView(view);
			content_txt=(EditText) view.findViewById(R.id.content_txt);
		    contact_txt=(EditText) view.findViewById(R.id.contact_txt);
			view.findViewById(R.id.commit_button).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(TextUtils.isEmpty(content_txt.getText())){
						Toast.makeText(context, "请输入您的意见！", 3000).show();
						return;
					}
					if(TextUtils.isEmpty(contact_txt.getText())){
						Toast.makeText(context, "请输入您的联系方式！", 3000).show();
						return;
					}
					new ResultAsyncTask<Response>(context) {

						@Override
						protected void onPostExecuteSuc(Response result) {
							// TODO Auto-generated method stub
							
							if(result.getSuccess()==1){
								Toast.makeText(context, "提交成功",3000).show();
								if(context instanceof FragmentActivity){
									if(!((FragmentActivity) context).getSupportFragmentManager().popBackStackImmediate()){
										((Activity) context).finish();
									}
								}else if(context instanceof Activity){
									((Activity) context).finish();
								}
							}else{
								Toast.makeText(context, "提交失败",3000).show();
							}
						}

						@Override
						protected Response doInBackground(Void... params) {
							// TODO Auto-generated method stub
							String url="http://faw-vw.allyes.com/index.php?g=api&m=feedback&a=add";
							ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
						/*	pairs.add(new BasicNameValuePair("g", "api"));
							pairs.add(new BasicNameValuePair("m", "feedback"));*/
							pairs.add(new BasicNameValuePair("typeid","2"));
							pairs.add(new BasicNameValuePair("content", content_txt.getText().toString()));
							pairs.add(new BasicNameValuePair("contact", contact_txt.getText().toString()));
							String json=HttpUtils.reqForGet(url, pairs);
				           try {
							return new Gson().fromJson(json, Response.class);
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							
							e.printStackTrace();
							return null;
						}
						}
						
					}.execute();
				}
			});
			//TODO 数据绑定、提交
			return topBar;
		}
}
