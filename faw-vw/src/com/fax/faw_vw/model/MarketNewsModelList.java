package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;

//市场活动，媒体声音 通用的JavaBean
public class MarketNewsModelList extends Respon{
	Body Body;
	class Body{
		ArrayList<NewsModel> list;
	}
	public ArrayList<NewsModel> getModels() {
		if(Body.list==null) return new ArrayList<MarketNewsModelList.NewsModel>();
		return Body.list;
	}
	
	/**
	"TITLE": "",
	"SUMMARY": "",
	"NEWS_TYPE": "",
	"DEALER_ID": "-1",
	"SERIES_IDS": "",
	"CONTENT": "",
	"BEGIN_TIME": "",
	"END_TIME": "",
	"BIG_PICTURE": "",
	"THUM_PICTURE": "",
	"UPDATE_TIME": "2014/10/20 14:30:37",
	"DELETE_FLAG": "False",
	"ID": "48333",
	"IS_TOP": "False",
	"STATUS": "1",
	"SOURCE_TYPE": "0",
	"NEWS_BIG_TYPE": "1",
	"CITY_ID": "-1"
	 */
	public class NewsModel implements Serializable{
		String TITLE;
        String SUMMARY;
        String CONTENT;
        String UPDATE_TIME;
		String THUM_PICTURE;
		public String getTITLE() {
			return TITLE;
		}
		public String getSUMMARY() {
			return SUMMARY;
		}
		public String getCONTENT() {
			return "<html>"
					+ "<head>"
					+ "<title>"+TITLE+"</title>"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>"
					+ "</head>"
					+ "<body>" + CONTENT +"</body>"
					+ "</html>";
		}
		public String getUPDATE_TIME() {
			return UPDATE_TIME;
		}
		public String getTHUM_PICTURE() {
			return THUM_PICTURE;
		}
		
	}
}
