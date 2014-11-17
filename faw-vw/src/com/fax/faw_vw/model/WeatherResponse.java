package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;


public class WeatherResponse implements Serializable{
	private int error;
	private String status;
	private String date;
	private ArrayList<Result> results;
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDate() {
		return date.replaceFirst("-", "年").replaceFirst("-", "月")+"日";
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public Result getResult() {
		if(results==null || results.size()==0) return null;
		return results.get(0);
	}

	public class Result implements Serializable{
		private String currentCity;
		private int pm25;
		private ArrayList<Weatherdata> weather_data;
		private ArrayList<Weatherindex> index;
		public ArrayList<Weatherindex> getIndex() {
			return index;
		}
		public void setIndex(ArrayList<Weatherindex> index) {
			this.index = index;
		}
		public String getCurrentCity() {
			return currentCity;
		}
		public void setCurrentCity(String currentCity) {
			this.currentCity = currentCity;
		}
		public int getPm25() {
			return pm25;
		}

		public Weatherdata getWeather_data() {
			return weather_data.get(0);
		}
		public void setWeather_data(ArrayList<Weatherdata> weather_data) {
			this.weather_data = weather_data;
		}
	public class Weatherdata implements Serializable {
				private String date;
				private String dayPictureUrl;
				private String nightPictureUrl;
				private String weather;
				private String wind;
				private String temperature;
				public String getDate() {
					return date;
				}
				public void setDate(String date) {
					this.date = date;
				}
				public String getDayPictureUrl() {
					return dayPictureUrl;
				}
				public void setDayPictureUrl(String dayPictureUrl) {
					this.dayPictureUrl = dayPictureUrl;
				}
				public String getNightPictureUrl() {
					return nightPictureUrl;
				}
				public void setNightPictureUrl(String nightPictureUrl) {
					this.nightPictureUrl = nightPictureUrl;
				}
				public String getWeather() {
					return weather;
				}
				public void setWeather(String weather) {
					this.weather = weather;
				}
				public String getWind() {
					return wind;
				}
				public void setWind(String wind) {
					this.wind = wind;
				}
				public String getTemperature() {
					return temperature;
				}
				public void setTemperature(String temperature) {
					this.temperature = temperature;
				}

			}
	public class Weatherindex implements Serializable {
				private String title;
				private  String zs;
				private String tipt;
				private  String des;
				public String getTitle() {
					return title;
				}
				public void setTitle(String title) {
					this.title = title;
				}
				public String getZs() {
					return zs;
				}
				public void setZs(String zs) {
					this.zs = zs;
				}
				public String getTipt() {
					return tipt;
				}
				public void setTipt(String tipt) {
					this.tipt = tipt;
				}
				public String getDes() {
					return des;
				}
				public void setDes(String des) {
					this.des = des;
				}
			}

	}
	
}
