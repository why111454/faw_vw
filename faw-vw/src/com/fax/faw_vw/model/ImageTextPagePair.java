package com.fax.faw_vw.model;

import android.support.v4.app.Fragment;

public class ImageTextPagePair extends ImageResPagePair{
	String text;
	public ImageTextPagePair(int imgResId, String text, Fragment fragment) {
		super(imgResId, fragment);
		this.text = text;
	}
	public String getText(){
		return text;
	}

}
