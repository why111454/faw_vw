package com.fax.faw_vw.fragments_car;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.FragmentContainLandscape;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fargment_common.PdfReaderFragment;
import com.fax.faw_vw.fragment_360.PackedFileLoader;
import com.fax.faw_vw.fragment_360.Show360FrameFragment;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.AssetFrameViewPagerFragment;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.task.DownloadTask;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

//下载中心
//将资源要放在asset中，图集参考com.fax.faw_vw.fragments_car.ShowCarHeadDetailFragment:52
//视频可以调用系统播放器（需要先将资源复制到储存卡）
//pdf阅读需要找一个可用的pdf库（放最后面做）

public class CarDownloadFragment extends MyFragment{
	String dirName;
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
					.setTitle("下载中心").setContentView(R.layout.car_download_center);
			
			//取到传入的车型，不同车型的下载中心页不一样
			final ShowCarItem carItem = getSerializableExtra(ShowCarItem.class);
			
			final ObjectXListView listView = (ObjectXListView) topBar.findViewById(android.R.id.list);
			listView.setPullRefreshEnable(false);
			listView.setAdapter(new ObjectXAdapter.SingleLocalGridPageAdapter<AssetFrame>(2){
				@Override
				public List<AssetFrame> instanceNewList() throws Exception {
					return FrameFactory.createFramesFromAsset(context, dirName, -1);
				}
				@Override
				protected View bindGridView(ViewGroup contain, AssetFrame t, int position, View convertView) {
					FrameLayout layout = (FrameLayout) convertView;
					if(layout==null){
						layout = new FrameLayout(context);
						layout.addView(new ImageView(context));
						layout.addView(new ImageView(context));
					}
					ImageView img1 = (ImageView) layout.getChildAt(0);
					img1.setAdjustViewBounds(true);
					int padding = (int) MyApp.convertToDp(8);
					img1.setPadding(padding, padding, padding, padding);
					img1.setImageDrawable(t.decodeDrawable(context));
					
					ImageView img2 = (ImageView) layout.getChildAt(1);
					img2.setScaleType(ScaleType.CENTER_INSIDE);
					if(!isLookPic()) img2.setImageResource(R.drawable.common_img_download);
					
					return layout;
				}
				@Override
				public void onItemClick(AssetFrame t, View view, int position, long id) {
					super.onItemClick(t, view, position, id);
					if(isLookPic()){//查看大图
						String dir = dirName.replace("pics", "bigPics");
						FragmentContain.start(getActivity(), 
								MyApp.createFragment(AssetFrameViewPagerFragment.class, dir, position));
						
					}else{
						String resUrl = getResUrl( ((AssetFrame)t).getPath() );
						//区分视频还是Pdf
						if(resUrl.endsWith(".mp4")){
							Intent intent = new Intent(Intent.ACTION_VIEW)
								.setDataAndType(Uri.parse(resUrl), "video/*");
					        startActivity(intent);
						}else if(resUrl.endsWith(".pdf")){
							downloadAndOpenPdf(resUrl);
						}
					}
				}
				@Override
				public void onLoadFinish(List<AssetFrame> allList) {
					super.onLoadFinish(allList);
					listView.setFootHint("");
				}
				@Override
				protected boolean isAutoLoadAfterInit() {
					return false;
				}
	        });
			
			RadioGroup radioGroup = (RadioGroup) topBar.findViewById(R.id.radio_group);
			radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
					if(!checkedBtn.isChecked()) return;//avoid check off callback
			        dirName = "download_center/"+carItem.getModel_en()+"/"+checkedBtn.getTag();
			        listView.reload();
				}
			});
			((CompoundButton)radioGroup.findViewById(R.id.car_download_rb_pics)).setChecked(true);
			return topBar;
		}
		private boolean isLookPic(){
			return dirName.endsWith("pics");
		}
		//返回资源的网络地址（结尾mp4或者pdf）
		private String getResUrl(String filePath){
			String fileNameWithOutEnd = new File(filePath).getName().replace(".jpg", "");
			if(dirName.contains("shouCe") || dirName.contains("zazhi")){
				return MyApp.ResUrl + "pdf/d_"+fileNameWithOutEnd + ".pdf";
			}else if(dirName.contains("TVC")){
				String url = MyApp.ResUrl + "video/";
				
				if(dirName.contains("BORA")) url+="bl";
				else if(dirName.contains("CC")) url+="cc";
				else if(dirName.contains("BORA")) url+="bl";
				else if(dirName.contains("GOLF")) url+="glof";
				else if(dirName.contains("JETTA")) url+="jd";
				else if(dirName.contains("MAGOTAN")) url+="mt";
				else if(dirName.contains("SAGITAR")) url+="st";
				
				url += ("/video"+fileNameWithOutEnd.replace("tvc", "") + ".mp4");
				return url;
			}
			return null;
		}
		private void downloadAndOpenPdf(String url){
			String name = new File(Uri.parse(url).getPath()).getName();
			final File file = new File(context.getExternalCacheDir(), name);
			final Runnable openRun = new Runnable() {
				@Override
				public void run() {
					FragmentContain.start(getActivity(), MyApp.createFragment(PdfReaderFragment.class, file));
				}
			};
			if(file.exists() && !file.isDirectory() && file.length()>0 ){
				openRun.run();
			}else{
				file.delete();
				final File dlFile = new File(file.getParent(), file.getName()+".dl");
				new DownloadTask(context, url, dlFile, true) {
					@Override
					protected void onPostExecuteSuc(File result) {
						dlFile.renameTo(file);
						openRun.run();
					}
				}.setProgressDialog().execute();
			}
		}
}
