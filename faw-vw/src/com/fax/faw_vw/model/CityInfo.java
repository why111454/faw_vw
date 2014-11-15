package com.fax.faw_vw.model;

public class CityInfo {
	String SystemAreaid;
	String AreaName;
	String AreaCode;
	String lat;
	String lng;
	
	public String getSystemAreaid() {
		return SystemAreaid;
	}

	public String getAreaName() {
		return AreaName;
	}

	public String getAreaCode() {
		return AreaCode;
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}

	public String toString(){
		return AreaName;
	}
}
