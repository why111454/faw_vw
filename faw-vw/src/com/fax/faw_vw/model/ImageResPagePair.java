package com.fax.faw_vw.model;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class ImageResPagePair {
	int imgResId;
	boolean isLandscape;
	Fragment fragment;
//	Class<? extends Activity> activityClass;
	public ImageResPagePair(int imgResId) {
		this.imgResId = imgResId;
	}
	public ImageResPagePair(int imgResId, Fragment fragment) {
		this.imgResId = imgResId;
		this.fragment = fragment;
	}
	public ImageResPagePair(int imgResId, Fragment fragment, boolean isLandscape) {
		this.imgResId = imgResId;
		this.fragment = fragment;
		this.isLandscape = isLandscape;
	}
//	public ImageResPagePair(int imgResId, Class<? extends Activity> activityClass) {
//		this.imgResId = imgResId;
//		this.activityClass = activityClass;
//	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof ImageResPagePair){
			ImageResPagePair another = (ImageResPagePair) o;
			if(another!=null && imgResId != another.imgResId) return true;
		}
		return super.equals(o);
	}
	public int getImgResId() {
		return imgResId;
	}
//	public Class<? extends Activity> getActivityClass() {
//		return activityClass;
//	}
	public Fragment getFragment() {
		return fragment;
	}
	public boolean isLandscape() {
		return isLandscape;
	}
}
