package com.fax.faw_vw.model;

import java.io.Serializable;

import com.amap.api.maps2d.model.LatLng;

public class Dealer implements Serializable{
	String id;
	String name;
	String fullname;
	String address;
	String selltel;
	String lon;
	String lat;

	public LatLng getLatLng(){
		return new LatLng(Double.valueOf(lat), Double.valueOf(lon));
	}
	public String getSnippet(){
		return "地址："+address+"\n电话："+selltel;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFullname() {
		return fullname;
	}

	public String getAddress() {
		return address;
	}

	public String getSelltel() {
		return selltel;
	}

	public String getGlon() {
		return lon;
	}

	public String getGlat() {
		return lat;
	}
	
}
