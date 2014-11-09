package com.fax.faw_vw.fragment_360;

import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_360.PackedFileLoader.PanoInfo;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.utils.task.ResultAsyncTask;

@SuppressLint("NewApi")
public class PanoFragment extends MyFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ShowCarItem carItem = getSerializableExtra(ShowCarItem.class);
		final PackedFileLoader fileLoader = PackedFileLoader.getInstance(context, carItem);
		ArrayList<PanoInfo> panoInfos = fileLoader.getPanoInfoList();
		
		final View view = inflater.inflate(R.layout.show360_pano, container, false);
		final WebView webView = (WebView) view.findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();

		settings.setJavaScriptEnabled(true);
		
		settings.setLoadsImagesAutomatically(true);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		
		CookieSyncManager.createInstance(context);
		CookieManager.getInstance().setAcceptCookie(true);
		
//		settings.setSupportZoom(true);
//		settings.setBuiltInZoomControls(true);
//		if(android.os.Build.VERSION.SDK_INT>=11) settings.setDisplayZoomControls(false);
    	
    	settings.setAppCacheEnabled(true);
    	settings.setDatabaseEnabled(true);
    	settings.setDomStorageEnabled(true);
    	
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		
		LinearLayout linear = (LinearLayout) view.findViewById(android.R.id.summary);
		for(int i=0,size=panoInfos.size(); i<size; i++){
			final PanoInfo panoInfo = panoInfos.get(i);
			ImageButton tv = new ImageButton(context);
//			tv.setText(touch360.color_name);
//			tv.setTextSize(12);
			tv.setBackgroundResource(R.drawable.common_btn_in_white);
			Drawable drawable = panoInfo.colorFrame.decodeDrawable(context);
			tv.setScaleType(ScaleType.CENTER_INSIDE);
			tv.setImageDrawable(drawable);
//			tv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
//			tv.setGravity(Gravity.CENTER);
			int padding = (int) MyApp.convertToDp(6);
			tv.setPadding(padding, padding, padding, padding);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//载入网页
					new ResultAsyncTask<String>(context) {
						@Override
						protected void onPostExecuteSuc(String result) {
							try {
								String html = IOUtils.toString(new FileInputStream(result),"UTF-8");
								String url = "file:///"+result;
								webView.loadUrl("about:blank");//清除页面，同clearView
								webView.loadDataWithBaseURL(url, html, null, "UTF-8", url);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						@Override
						protected String doInBackground(Void... params) {
							return fileLoader.unpackPanoFiles(panoInfo, context.getExternalCacheDir().getPath()+"/"+carItem.getModel_en());
						}
					}.setProgressDialog().execute();
				}
			});
			if(i==0){//载入第一个
				tv.performClick();
			}
			linear.addView(tv, new LinearLayout.LayoutParams((int) MyApp.convertToDp(50), -1));
		}
		view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backStack();
			}
		});
		return view;
	}
	
}
