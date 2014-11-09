package com.fax.faw_vw.model;

import java.io.Serializable;
import java.net.URLEncoder;

import com.fax.faw_vw.util.HanziToPinyin;
import android.util.Log;

public class ShowCarItemRes implements Serializable{
	int resId360;
	ShowCarDetailHead[] heads = new ShowCarDetailHead[5];
	
	//速腾专用，包裹其他的详情
	CarItemChild[] itemChildren;//如果这个属性存在，详情页将会展示这个
	
	public ShowCarItemRes(int resId360, ShowCarDetailHead... heads) {
		this.resId360 = resId360;
		System.arraycopy(heads, 0, this.heads, 0, Math.min(heads.length, 5));
	}
	
	public ShowCarItemRes(int resId360, CarItemChild[] itemChildren) {
		this.resId360 = resId360;
		this.itemChildren = itemChildren;
	}

	public int getResId360() {
		return resId360;
	}

	public void setResId360(int resId360) {
		this.resId360 = resId360;
	}
	
	public ShowCarDetailHead[] getHeads() {
		return heads;
	}

	public CarItemChild[] getItemChildren() {
		return itemChildren;
	}

	public void setHeads(ShowCarDetailHead[] heads) {
		this.heads = heads;
	}
	
	public static class ShowCarDetailHead implements Serializable{
		int id;
		int resId;
		String title;
		public ShowCarDetailHead(int id, int resId, String title) {
			this.id = id;
			this.resId = resId;
			this.title = title;
		}
		public int getId() {
			return id;
		}
		public int getResId() {
			return resId;
		}
		public String getTitle() {
			return title;
		}
		public String getAssetDir(String codelNameEn) {
			String dir = "car_detail/"+codelNameEn+"/"+HanziToPinyin.getFullPinYin(title).toLowerCase();
			Log.d("fax", "getAssetDir : " + dir);
			return dir;
		}
	}
	
	public static class CarItemChild implements Serializable{
		int imgResId;
		String name;
		String name_en;
		ShowCarItemRes itemRes;
		public CarItemChild(int imgResId, String name, String name_en, ShowCarItemRes itemRes) {
			super();
			this.imgResId = imgResId;
			this.name = name;
			this.name_en = name_en;
			this.itemRes = itemRes;
		}
		public int getImgResId() {
			return imgResId;
		}
		public String getName() {
			return name;
		}
		public String getNameEn() {
			return name_en;
		}
		public ShowCarItemRes getItemRes() {
			return itemRes;
		}
	}
}
