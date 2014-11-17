package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;


public class IllegalInfo implements Serializable{
	
	private int error_code;
	private String reason;
	private result result;
	
	
	public int getError_code() {
		return error_code;
	}


	public void setError_code(int error_code) {
		this.error_code = error_code;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public result getResult() {
		return result;
	}


	public void setResult(result result) {
		this.result = result;
	}


	public class result implements Serializable{
		private String province;
		private String city;
		private String hphm;
		private ArrayList<lists> lists;
		
		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getHphm() {
			return hphm;
		}

		public void setHphm(String hphm) {
			this.hphm = hphm;
		}

		public ArrayList<lists> getLists() {
			return lists;
		}

		public void setLists(ArrayList<lists> lists) {
			this.lists = lists;
		}

		public class lists implements Serializable{
	
			private String date;
			private String area;
			private String act;
			private String code;
			private String fen;
			private String money;
			private String handled;
			private String longitude;
			private String latitude;
			private String PayNo;
			private String CollectOrgan;
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			public String getArea() {
				return area;
			}
			public void setArea(String area) {
				this.area = area;
			}
			public String getAct() {
				return act;
			}
			public void setAct(String act) {
				this.act = act;
			}
			public String getCode() {
				return code;
			}
			public void setCode(String code) {
				this.code = code;
			}
			public String getFen() {
				return fen;
			}
			public void setFen(String fen) {
				this.fen = fen;
			}
			public String getMoney() {
				return money;
			}
			public void setMoney(String money) {
				this.money = money;
			}
			public String getHandled() {
				return handled;
			}
			public void setHandled(String handled) {
				this.handled = handled;
			}
			public String getLongitude() {
				return longitude;
			}
			public void setLongitude(String longitude) {
				this.longitude = longitude;
			}
			public String getLatitude() {
				return latitude;
			}
			public void setLatitude(String latitude) {
				this.latitude = latitude;
			}
			public String getPayNo() {
				return PayNo;
			}
			public void setPayNo(String payNo) {
				PayNo = payNo;
			}
			public String getCollectOrgan() {
				return CollectOrgan;
			}
			public void setCollectOrgan(String collectOrgan) {
				CollectOrgan = collectOrgan;
			}
			
		}
		
	}
}
