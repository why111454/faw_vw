package com.fax.faw_vw.fragment_360;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fax.faw_vw.fragment_360.PackedFileLoader.FrameInfo.KeyPoint;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.frameAnim.ZipBitmapFrame;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;


public class PackedFileLoader {
	public static PackedFileLoader getInstance(Context context, ShowCarItem carItem){
		final File file = new File(context.getExternalCacheDir(),carItem.get360ZipFileName());
		if(file.exists() && !file.isDirectory() && file.length()>0 ){
			return getInstance(file);
			
		}return null;
	}
	
 	private static HashMap<String, PackedFileLoader> map =new HashMap<String, PackedFileLoader>();
	public static synchronized PackedFileLoader getInstance(File file){
		PackedFileLoader fileLoader = map.get(file.getPath());
		if(fileLoader==null){
			fileLoader = new PackedFileLoader(file);
			map.put(file.getPath(), fileLoader);
		}
		return fileLoader;
	}
	String carName;
	ZipFile zipFile;
	PackJsonInfo jsonInfo;
	private PackedFileLoader(File file){
		try {
			zipFile = new ZipFile(file);
			carName = file.getName().substring(0, file.getName().lastIndexOf("."));
			InputStream is = zipFile.getInputStream(zipFile.getEntry(carName+"/"+carName+"_config.json"));
			jsonInfo = new Gson().fromJson(new InputStreamReader(is), PackJsonInfo.class);
			//key frame 部分
			FrameInfo last=null;
			String format = carName+"/key/"+carName+"_key_%d.jpg";
			for(FrameInfo frameInfo : jsonInfo.key){
				if(last!=null){
					last.setFrames(FrameFactory.createFramesFromZip(zipFile, format, last.getKeyFrame(), frameInfo.getKeyFrame()+1, 30));
				}
				last = frameInfo;
			}
			//touch 360部分
			for(Touch360 touch360: jsonInfo.touch360s){
				format = carName+"/360/"+touch360.color_360_name+"%d.png";
				touch360.setFrames(FrameFactory.createFramesFromZip(zipFile, format, 0, 100, 30));
				touch360.setBtnFrame(new ZipBitmapFrame(zipFile, carName+"/360/"+touch360.color_button_name));
			}
			//pano 部分
			for(PanoInfo panoInfo : jsonInfo.panoInfos){
				panoInfo.colorFrame = new ZipBitmapFrame(zipFile, carName+"/pano/"+panoInfo.color_button_name);
			}
//			for(ZipEntry entry : Collections.list(zipFile.entries())){
//				if(entry.getName().contains("mp4")) Log.d("fax", "entry:"+entry.getName());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recycle(){
		try {
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.remove(zipFile.getName());
	}
	
	public ArrayList<FrameInfo> getFrameInfoList(){
		return jsonInfo.key;
	}
	public ArrayList<Touch360> getTouch360List(){
		return jsonInfo.touch360s;
	}
	public ArrayList<PanoInfo> getPanoInfoList(){
		return jsonInfo.panoInfos;
	}
	
	/**从Zip读出KeyPoint的输入流 */
	public InputStream readKeyPointImageFile(KeyPoint keyPoint) throws Exception{
		return zipFile.getInputStream(zipFile.getEntry(carName+"/hot/"+keyPoint.show_image));
	}
	/**从Zip读出KeyPoint的视频输入流 */
	public InputStream readKeyPointMp4File(KeyPoint keyPoint) throws Exception{
		return zipFile.getInputStream(zipFile.getEntry(carName+"/mp4/"+keyPoint.getFilename()));
	}
	/**
	 * 解压出全景文件到,耗时操作
	 * @return 解压出的主页的文件的路径
	 */
	public String unpackPanoFiles(PanoInfo panoInfo, String writeDir){
		String id = panoInfo.getId();
		String path = carName+"/pano/"+id;
		for(ZipEntry entry : Collections.list(zipFile.entries())){
			if(entry.isDirectory()) continue;
			if(entry.getName().startsWith(path)){
				String name = new File(entry.getName()).getName();
				File toFile = new File(writeDir, id+"/"+name);
				if(toFile.length()==entry.getSize()) continue;
				toFile.getParentFile().mkdirs();
				try {
					IOUtils.copy(zipFile.getInputStream(entry), new FileOutputStream(toFile));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return writeDir +"/" + id + "/" + carName +".html";
	}
	
	class PackJsonInfo{
		ArrayList<FrameInfo> key;
		@SerializedName(value = "360")
		ArrayList<Touch360> touch360s;
		@SerializedName(value = "pano")
		ArrayList<PanoInfo> panoInfos;
	}
	public class FrameInfo{
		String title;
		int keyframe;
		@SerializedName(value = "keypoint")
		KeyPoint[] keyPoints;
		int getKeyFrame(){
			return keyframe;
		}
		
		List<? extends Frame> frames;
		public List<? extends Frame> getFrames() {
			return frames;
		}
		public void setFrames(List<? extends Frame> frames) {
			this.frames = frames;
		}
		public KeyPoint[] getKeyPoints() {
			return keyPoints;
		}
		public class KeyPoint{
			int hot_x,hot_y,image_x,image_y;
			String show_image, type, filename;
			String hotDir;
			public String getHotDir() {
				return hotDir;
			}
			public void setHotDir(String hotDir) {
				this.hotDir = hotDir;
			}
			public int getHot_x() {
				return hot_x;
			}
			public int getHot_y() {
				return hot_y;
			}
			public int getImage_x() {
				return image_x;
			}
			public int getImage_y() {
				return image_y;
			}
			public String getShow_image() {
				return show_image;
			}
			public String getType() {
				return type;
			}
			public String getFilename() {
				return filename+"."+type;
			}
			
		}
	}
	public class Touch360{
//        "color_name":"珊瑚蓝",
//        "color_button_name":"jetta_color_0.png",
//        "color_360_name":"jetta_blue_"
		String color_name;
		String color_button_name;
		String color_360_name;

		List<? extends Frame> frames;
		Frame btnFrame;
		public List<? extends Frame> getFrames() {
			return frames;
		}
		public void setFrames(List<? extends Frame> frames) {
			this.frames = frames;
		}
		public Frame getBtnFrame() {
			return btnFrame;
		}
		public void setBtnFrame(Frame btnFrame) {
			this.btnFrame = btnFrame;
		}
	}
	public class PanoInfo{
		String color_name;
		String color_button_name;
		String id;
		Frame colorFrame;
		public String getColor_name() {
			return color_name;
		}
		public String getColor_button_name() {
			return color_button_name;
		}
		public String getId() {
			return id.split("_")[1];//取 cc_0 中的 0
		}
	}
}
