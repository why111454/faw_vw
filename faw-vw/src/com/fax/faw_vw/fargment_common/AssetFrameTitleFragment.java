package com.fax.faw_vw.fargment_common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.views.FitWidthImageView;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.FrameAnimation;

public class AssetFrameTitleFragment extends MyFragment {
	public static AssetFrameTitleFragment createInstance(String path, String title){
		return (AssetFrameTitleFragment) MyApp.createFragment(AssetFrameTitleFragment.class, 0,
				new String[]{path, title});
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String[] pathAndTitle = getSerializableExtra(String[].class);
		String path = pathAndTitle[0];
		String title = pathAndTitle[1];
		ImageView imageView = new FitWidthImageView(context);
		FrameAnimation.setFrameToView(imageView, new AssetFrame(path));
		
		ScrollView scrollView = new ScrollView(context);
		scrollView.addView(imageView, -1, -2);
		return new MyTopBar(context).setLeftBack().setTitle(title).setContentView(scrollView);
	}

}
