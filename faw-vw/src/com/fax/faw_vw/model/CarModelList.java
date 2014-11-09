package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.fax.faw_vw.MyApp;

public class CarModelList extends Respon{
	ArrayList<CarModel> msg;
	
	public ArrayList<CarModel> getMsg() {
		return msg;
	}

	public class CarModel implements Serializable{
		String id;
		String title;
		String model_name;
		String comparepic;
		String price;
		public String getId() {
			return id;
		}
		public String getTitle() {
			return title;
		}
		public String getModel_name() {
			return model_name;
		}
		public String getComparepic() {
			return comparepic;
		}
		public String getPrice() {
			return price;
		}
		public String getComparepicUrl() {
			return MyApp.Host + comparepic;
		}
		
	}
}
