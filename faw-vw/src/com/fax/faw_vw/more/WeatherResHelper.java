package com.fax.faw_vw.more;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**获取assets中的资源文件（Android工程里assets文件不能出现中文名，就把天气资源打包成zip了） */
public class WeatherResHelper {
	/**
	 * 获取天气Icon的图片
	 * @param context 上下文
	 * @param weather Api获取的到天气，如“晴转多云”
	 * @return 图片对象
	 */
	public static Bitmap getIcon(Context context, String weather){
		return getInstance(context).getIcon(weather);
	}
	/**
	 * 获取天气背景的图片
	 * @param context 上下文
	 * @param weather Api获取的到天气，如“晴转多云”
	 * @return 图片对象
	 */
	public static Bitmap getBg(Context context, String weather){
		return getInstance(context).getBg(weather);
	}
	
	private static WeatherResHelper resHelper;
	private static WeatherResHelper getInstance(Context context){
		if(resHelper == null) resHelper = new WeatherResHelper(context);
		return resHelper;
	}
	ZipFile zipFile;
	private WeatherResHelper(Context context) {
		try {
			File cacheFile = new File(context.getCacheDir(), "weather_res.zip");
			AssetManager assetManager = context.getAssets();
			if(!cacheFile.exists() || cacheFile.isDirectory()){
				cacheFile.delete();
				FileUtils.copyInputStreamToFile(assetManager.open("weather_res.zip"), cacheFile);
			}
			zipFile = new ZipFile(cacheFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Bitmap getIcon(String weather){
		return decodeBitmap(getFileName(weather)+".png");
	}
	private Bitmap getBg(String weather){
		return decodeBitmap(getFileName(weather)+"_背景.jpg");
	}
	private Bitmap decodeBitmap(String fileName){
		try {
			InputStream is = zipFile.getInputStream(zipFile.getEntry(fileName));
			return BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	}

	private String getFileName(String weather) {
		String name = weather;
		int index;//XX转XX，那么以后面的天气为准
		if( (index = name.indexOf("转"))>=0){
			name = name.substring(index+1);
		}
		
		if (weather.contains("冰雹")) {
			name = "冰雹";
		} else if (weather.contains("雾") || weather.contains("沙尘") 
				|| weather.contains("浮尘") || weather.contains("扬沙")) {
			name = "雾霾";
		} else if (weather.contains("台风")) {
			name = "台风";
		} else if (weather.contains("风")) {
			name = "暴风";
		} else if (weather.contains("雨") && weather.contains("雪")) {
			name = "雨夹雪";
		} else if (weather.contains("阵雨")) {
			name = "雷阵雨";
		} else if (weather.contains("小雨") || weather.contains("冻雨")) {
			name = "小雨";
		} else if (weather.contains("大雨") || weather.contains("暴雨")) {
			name = "大雨";
		} else if (weather.contains("雨")) {
			name = "中雨";
		} else if (weather.contains("阵雪") || weather.contains("小雪")) {
			name = "小雪";
		} else if (weather.contains("大雪") || weather.contains("暴雪")) {
			name = "大雪";
		} else if (weather.contains("雪")) {
			name = "中雪";
		} else if (weather.contains("晴")) {
			name = "晴";
		} else if (weather.contains("云")) {
			name = "多云";
		} else if (weather.contains("阴")) {
			name = "阴";
		}
		return name;
	}
}
