package com.fax.faw_vw.fargment_common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.R.string;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.DownloadHandler;
import com.fax.utils.view.TopBarContain;

public class LocalWebViewFragment extends MyFragment implements DownloadListener {
	public static final String Extra_Data="data";
	public static final String Extra_Title="title";

    public static void start(Context context, String data, String title){
    	FragmentContain.start((Activity)context, LocalWebViewFragment.class, 
    			new Intent().putExtra(Extra_Data, data).putExtra(Extra_Title, title), -1);
    }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		WebView webView=new WebView(context);
		final TopBarContain topBarContain=new MyTopBar(context)
				.setLeftBack()
                .setTitle(R.string.Task_PleaseWait)
				.setContentView(webView);
		String title = getArguments().getString(Extra_Title);
		if(!TextUtils.isEmpty(title)){
			topBarContain.setTitle(title);
		}
		
		WebSettings settings = webView.getSettings();
		// User settings
		settings.setJavaScriptEnabled(true);
		settings.setLoadsImagesAutomatically(true);
//		settings.setUseWideViewPort(true);
//		settings.setLoadWithOverviewMode(true);
		CookieSyncManager.createInstance(context);
		CookieManager.getInstance().setAcceptCookie(true);
		
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		if(android.os.Build.VERSION.SDK_INT>=11) settings.setDisplayZoomControls(false);
		settings.setSupportMultipleWindows(false);
		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		webView.setDrawingCacheEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				topBarContain.setProgress(newProgress);
			}
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				topBarContain.setTitle(title);
			}
		});
        String data = null;
        if(getArguments()!=null){
        	data = getArguments().getString(Extra_Data);
        	if(data== null) data = getSerializableExtra(String.class);
        }
        
		webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		webView.setDownloadListener(this);
		
		return (topBarContain);
	}

	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
			long contentLength) {
		DownloadHandler.onDownloadStartNoStream(getActivity(), url, mimetype);
	}
	
}
