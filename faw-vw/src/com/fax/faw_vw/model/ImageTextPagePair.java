package com.fax.faw_vw.model;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.game.OnlineDriveGamePreStartFrag;

import android.support.v4.app.Fragment;

public class ImageTextPagePair extends ImageResPagePair{
	String text;
	public ImageTextPagePair(int imgResId, String text, Class<? extends Fragment> c) {
		this(imgResId, text, MyApp.createFragment(c));
	}
	public ImageTextPagePair(int imgResId, String text, Fragment fragment) {
		super(imgResId, fragment);
		this.text = text;
	}
	public ImageTextPagePair(int imgResId, String text, Class<? extends Fragment> c, boolean isLandscape) {
		this(imgResId, text, MyApp.createFragment(c));
		this.isLandscape = isLandscape;
	}
	public ImageTextPagePair(int imgResId, String text) {
		super(imgResId, null);
		this.text = text;
	}
	public String getText(){
		return text;
	}

}
