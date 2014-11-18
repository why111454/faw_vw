package com.fax.faw_vw.model;

import java.util.ArrayList;

import com.amap.api.maps2d.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class CarInfoList {
	ArrayList<Province> cmd;
	
	public ArrayList<Province> getData() {
		return cmd;
	}

	public class Province{
		String cityName;
		String engineNumber;
		String plateNumber;
		String vehicleIdNumber;
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public String toString(){
			return plateNumber;
		}
		public String getEngineNumber() {
			return engineNumber;
		}
		public void setEngineNumber(String engineNumber) {
			this.engineNumber = engineNumber;
		}
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
		}
		public String getVehicleIdNumber() {
			return vehicleIdNumber;
		}
		public void setVehicleIdNumber(String vehicleIdNumber) {
			this.vehicleIdNumber = vehicleIdNumber;
		}
		
	}
}
