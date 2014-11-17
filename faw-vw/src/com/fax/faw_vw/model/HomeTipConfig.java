package com.fax.faw_vw.model;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import android.content.Context;

//首页爱车贴士的随机文字
public class HomeTipConfig {
	private static HomeTipConfig homeTipConfig;
	public static String getATip(Context context){
		if(homeTipConfig==null){
			try {
				String json = IOUtils.toString(context.getAssets().open("jjConfig.json"));
				homeTipConfig = new Gson().fromJson(json, HomeTipConfig.class);
			} catch (Exception e) {
			}
		}
		return homeTipConfig.getATip();
	}
	
	HashMap<String, String>[] cmd;
	Random r = new Random();
	public String getATip(){
		Calendar calendar = Calendar.getInstance();
		int cmdIndex = calendar.get(Calendar.MONTH)/3;//0-3代表春夏秋冬
		List<String> values = new ArrayList<String>(cmd[cmdIndex].values());
		return values.get(r.nextInt(values.size()));
	}
}
