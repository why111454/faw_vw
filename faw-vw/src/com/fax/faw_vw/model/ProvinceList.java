package com.fax.faw_vw.model;

import java.util.ArrayList;

import com.amap.api.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class ProvinceList {
	ArrayList<Province> cmd;
	
	public ArrayList<Province> getData() {
		return cmd;
	}

	public class Province{
		String AreaName;
		String value;
		@SerializedName(value="lon")//XXX 因为json数据里的经纬度写反了
		double lat;
		@SerializedName(value="lat")
		double lon;
		public String getName() {
			return AreaName;
		}
		public String toString(){
			return AreaName;
		}
		public String getAreaCode(){
			return value;
		}
		public LatLng getLatLng(){
			return new LatLng(lat, lon);
		}
	}
}
