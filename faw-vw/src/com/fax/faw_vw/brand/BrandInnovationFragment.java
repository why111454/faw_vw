package com.fax.faw_vw.brand;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.fargment_common.AssetFrameTitleFragment;
import com.fax.faw_vw.views.FitWidthImageView;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.faw_vw.views.clickshow.ClickShowImageView;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

public class BrandInnovationFragment extends MyFragment {
	String[] titles = new String[]{
			"蓝驱科技","TSI涡轮缸内直喷技术","直接换挡变速器",
			"激光焊应用技术","自适应巡航系统","智能泊车辅助系统"
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final List<AssetFrame> imgs = FrameFactory.createFramesFromAsset(context, "brand/tec/imgs", 0);
		final List<AssetFrame> bigImgs = FrameFactory.createFramesFromAsset(context, "brand/tec/bigImgs", 0);
		ObjectXListView listView = new ObjectXListView(context);
		listView.setAdapter(new ObjectXAdapter.SingleLocalGridPageAdapter<AssetFrame>(2) {
			@Override
			public List<AssetFrame> instanceNewList() throws Exception {
				return imgs;
			}
			@Override
			protected View bindGridView(ViewGroup contain, AssetFrame t, int position, View convertView) {
				int padding = (int) MyApp.convertToDp(4);
				contain.setPadding(padding, padding, padding, padding);
				
				if(convertView==null){
					convertView = new FitWidthImageView(context);
					((ImageView)convertView).setAdjustViewBounds(true);
				}
				FrameAnimation.setFrameToView(convertView, t);
				return convertView;
			}
			@Override
			public void onItemClick(AssetFrame t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);
				addFragment(AssetFrameTitleFragment.createInstance(
						bigImgs.get(position).getPath(), titles[position]));
			}
			
		});
		return new MyTopBar(context).setLeftBack().setTitle("创新科技").setContentView(listView);
	}

}
