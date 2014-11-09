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

public class WebViewFragment extends MyFragment implements DownloadListener {
	public static final String Extra_Url="url";

    public static String EmptyUrlLoad = "http://www.baidu.com";
    public static void start(Context context, String url){
    	FragmentContain.start((Activity)context, MyApp.createFragment(WebViewFragment.class, url));
    }
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		WebView webView=new WebView(context);
		final TopBarContain topBarContain=new MyTopBar(context)
				.setLeftBack()
                .setTitle(R.string.Task_PleaseWait)
				.setContentView(webView);
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
        String url = null;
        if(getArguments()!=null){
        	url = getArguments().getString(Extra_Url);
        	if(url== null) url = getSerializableExtra(String.class);
        }
        if(TextUtils.isEmpty(url)) url = EmptyUrlLoad;
        else if(!url.startsWith("http://")&&!url.startsWith("https://")) url = "http://" + url;
        
		webView.loadUrl( url);
		webView.setDownloadListener(this);
		
		return (topBarContain);
	}

	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
			long contentLength) {
		DownloadHandler.onDownloadStartNoStream(getActivity(), url, mimetype);
	}
	
}
