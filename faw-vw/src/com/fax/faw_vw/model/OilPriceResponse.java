package com.fax.faw_vw.model;
import java.io.Serializable;
import java.util.ArrayList;


public class OilPriceResponse implements Serializable {
	private int error_code;
	private String reason;
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
	private result result;
	public class result implements Serializable {
		private ArrayList<data> data;
		public ArrayList<data> getData() {
			return data;
		}
		public void setData(ArrayList<data> data) {
			this.data = data;
		}
		public class data implements Serializable{
			private String name;
			private String area;
			private String areaname;
			public String getAreaname() {
				return areaname;
			}
			public void setAreaname(String areaname) {
				this.areaname = areaname;
			}
			private String address;
			private String brandname;
			private String type;
			private String discount;
			private String exhaust;
			private String position;
			private String lat;
			private String lon;
			private price price;
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getArea() {
				return area;
			}

			public void setArea(String area) {
				this.area = area;
			}

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}

			public String getBrandname() {
				return brandname;
			}
			public void setBrandname(String brandname) {
				this.brandname = brandname;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getDiscount() {
				return discount;
			}

			public void setDiscount(String discount) {
				this.discount = discount;
			}

			public String getExhaust() {
				return exhaust;
			}

			public void setExhaust(String exhaust) {
				this.exhaust = exhaust;
			}

			public String getPosition() {
				return position;
			}

			public void setPosition(String position) {
				this.position = position;
			}

			public String getLat() {
				return lat;
			}

			public void setLat(String lat) {
				this.lat = lat;
			}

			public String getLon() {
				return lon;
			}

			public void setLon(String lon) {
				this.lon = lon;
			}


			public price getPrice() {
				return price;
			}
			public void setPrice(price price) {
				this.price = price;
			}


			public class price implements Serializable{
				private String E90;
				private String E93;
				private String E97;
				private String E0;
				public String getE90() {
					return E90;
				}
				public void setE90(String e90) {
					E90 = e90;
				}
				public String getE93() {
					return E93;
				}
				public void setE93(String e93) {
					E93 = e93;
				}
				public String getE97() {
					return E97;
				}
				public void setE97(String e97) {
					E97 = e97;
				}
				public String getE0() {
					return E0;
				}
				public void setE0(String e0) {
					E0 = e0;
				}
			}
		}
	}
	

}
