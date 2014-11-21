package com.fax.faw_vw.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchDeclarBody implements Serializable{
	private String Success;
	private String Message;
	private ArrayList<delcar> Body;
	public String getSuccess() {
		return Success;
	}

	public void setSuccess(String success) {
		Success = success;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public ArrayList<delcar> getBody() {
		return Body;
	}

	public void setBody(ArrayList<delcar> body) {
		Body = body;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}

	private String Count;
	
	public class delcar implements Serializable{
/*	    "DEALER_NAME": "\u8425\u53e3\u7ea2\u8fd0\u6c47\u4f17\u6c7d\u8f66\u9500\u552e\u670d\u52a1\u6709\u9650\u516c\u53f8",
        "FK_CITY_ID": "1",
        "FK_PROVINCE_ID": "1",
        "ADDRESS": "\u8fbd\u5b81\u7701\u8425\u53e3\u5e02\u7ad9\u524d\u533a\u6e24\u6d77\u5927\u8857\u4e1c97\u53f7",
        "BRAND_ID": "0",
        "INTRODUCTION": "",
        "DEALER_SUMMARY": "",
        "SELL_OFF": "",
        "DEALER_URL": "",
        "SALES_PHONE": "0417-2951111-801",
        "AFTER_SALES_PHONE": "0417-2951111-802",
        "QQ": "",
        "EMAIL": "S721027@part.faw-vw.com",
        "SERVICE_PHONE": "0417-3331111",
        "LONGITUDE": "122.2031",
        "LATITUDE": "40.66111",
        "DEALER_ID": "1",
        "UPDATE_TIME": "2014/11/5 10:05:18",
        "DELETE_FLAG": "False",
        "ABOUT_TIME": ""*/
		private String DEALER_NAME;
		private String FK_CITY_ID;
		private String FK_PROVINCE_ID;
		private String ADDRESS;
		private String BRAND_ID;
		private String INTRODUCTION;
		private String DEALER_SUMMARY;
		private String SELL_OFF;
		private String LONGITUDE;
		private String LATITUDE;
		private String DELETE_FLAG;
		private String ABOUT_TIME;
		private String UPDATE_TIME;
		private String DEALER_URL;
		private String SALES_PHONE;
		private String AFTER_SALES_PHONE;
		private String QQ;
		private String EMAIL;
		private String SERVICE_PHONE;
		private String DEALER_ID;
		public String getDEALER_NAME() {
			return DEALER_NAME;
		}
		public void setDEALER_NAME(String dEALER_NAME) {
			DEALER_NAME = dEALER_NAME;
		}
		public String getFK_CITY_ID() {
			return FK_CITY_ID;
		}
		public void setFK_CITY_ID(String fK_CITY_ID) {
			FK_CITY_ID = fK_CITY_ID;
		}
		public String getFK_PROVINCE_ID() {
			return FK_PROVINCE_ID;
		}
		public void setFK_PROVINCE_ID(String fK_PROVINCE_ID) {
			FK_PROVINCE_ID = fK_PROVINCE_ID;
		}
		public String getADDRESS() {
			return ADDRESS;
		}
		public void setADDRESS(String aDDRESS) {
			ADDRESS = aDDRESS;
		}
		public String getBRAND_ID() {
			return BRAND_ID;
		}
		public void setBRAND_ID(String bRAND_ID) {
			BRAND_ID = bRAND_ID;
		}
		public String getINTRODUCTION() {
			return INTRODUCTION;
		}
		public void setINTRODUCTION(String iNTRODUCTION) {
			INTRODUCTION = iNTRODUCTION;
		}
		public String getDEALER_SUMMARY() {
			return DEALER_SUMMARY;
		}
		public void setDEALER_SUMMARY(String dEALER_SUMMARY) {
			DEALER_SUMMARY = dEALER_SUMMARY;
		}
		public String getSELL_OFF() {
			return SELL_OFF;
		}
		public void setSELL_OFF(String sELL_OFF) {
			SELL_OFF = sELL_OFF;
		}
		public String getLONGITUDE() {
			return LONGITUDE;
		}
		public void setLONGITUDE(String lONGITUDE) {
			LONGITUDE = lONGITUDE;
		}
		public String getLATITUDE() {
			return LATITUDE;
		}
		public void setLATITUDE(String lATITUDE) {
			LATITUDE = lATITUDE;
		}
		public String getDELETE_FLAG() {
			return DELETE_FLAG;
		}
		public void setDELETE_FLAG(String dELETE_FLAG) {
			DELETE_FLAG = dELETE_FLAG;
		}
		public String getABOUT_TIME() {
			return ABOUT_TIME;
		}
		public void setABOUT_TIME(String aBOUT_TIME) {
			ABOUT_TIME = aBOUT_TIME;
		}
		public String getUPDATE_TIME() {
			return UPDATE_TIME;
		}
		public void setUPDATE_TIME(String uPDATE_TIME) {
			UPDATE_TIME = uPDATE_TIME;
		}
		public String getDEALER_URL() {
			return DEALER_URL;
		}
		public void setDEALER_URL(String dEALER_URL) {
			DEALER_URL = dEALER_URL;
		}
		public String getSALES_PHONE() {
			return SALES_PHONE;
		}
		public void setSALES_PHONE(String sALES_PHONE) {
			SALES_PHONE = sALES_PHONE;
		}
		public String getAFTER_SALES_PHONE() {
			return AFTER_SALES_PHONE;
		}
		public void setAFTER_SALES_PHONE(String aFTER_SALES_PHONE) {
			AFTER_SALES_PHONE = aFTER_SALES_PHONE;
		}
		public String getQQ() {
			return QQ;
		}
		public void setQQ(String qQ) {
			QQ = qQ;
		}
		public String getEMAIL() {
			return EMAIL;
		}
		public void setEMAIL(String eMAIL) {
			EMAIL = eMAIL;
		}
		public String getSERVICE_PHONE() {
			return SERVICE_PHONE;
		}
		public void setSERVICE_PHONE(String sERVICE_PHONE) {
			SERVICE_PHONE = sERVICE_PHONE;
		}
		public String getDEALER_ID() {
			return DEALER_ID;
		}
		public void setDEALER_ID(String dEALER_ID) {
			DEALER_ID = dEALER_ID;
		}
		

		
	}
		 

}
