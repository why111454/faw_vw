package com.fax.faw_vw.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;
import com.fax.utils.view.pager.SamePagerAdapter;

public class OnlineDriveGamePreStartFrag extends MyFragment {
	private static ShowCarItem[] CHOOSE_CAR_ITEMS = new ShowCarItem[]{
			ShowCarItem.SHOW_CAR_ITEM_CC,
			ShowCarItem.SHOW_CAR_ITEM_BORA,
			ShowCarItem.SHOW_CAR_ITEM_GOLF,
			ShowCarItem.SHOW_CAR_ITEM_JETTA,
			ShowCarItem.SHOW_CAR_ITEM_MAGOTAN,
			ShowCarItem.SHOW_CAR_ITEM_SAGITAR,
		};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ShowCarItem showCarItem = getSerializableExtra(ShowCarItem.class);
		if(showCarItem!=null){//已选中了车型
			View view = inflater.inflate(R.layout.online_drive_game_pre_start, container, false);
			view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
					startActivity(new Intent(context, OnlineDriveGameActivity.class)
								.putExtra(ShowCarItem.class.getName(), showCarItem));
				}
			});
			return view;
			
		}else{//没有选中车型，再次选一遍
			View view = inflater.inflate(R.layout.online_drive_game_choose_car, container, false);
			final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
			viewPager.setAdapter(new SamePagerAdapter<AssetFrame>(FrameFactory.createFramesFromAsset(context, "online_drive_game/choose_cars", -1)) {
				@Override
				public View getView(AssetFrame t, int position, View convertView) {
					if(convertView==null){
						convertView = new ImageView(context);
					}
					FrameAnimation.setFrameToView(convertView, t);
					return convertView;
				}
			});

			view.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().finish();
					startActivity(new Intent(context, OnlineDriveGameActivity.class)
								.putExtra(ShowCarItem.class.getName(), CHOOSE_CAR_ITEMS[viewPager.getCurrentItem()]));
				}
			});
			view.findViewById(android.R.id.closeButton).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					backStack();
				}
			});
			return view;
		}
		
	}

}
