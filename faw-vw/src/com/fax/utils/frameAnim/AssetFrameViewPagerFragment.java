package com.fax.utils.frameAnim;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.utils.view.pager.SamePagerAdapter;
import com.fax.utils.view.photoview.PhotoView;

public class AssetFrameViewPagerFragment extends MyFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewPager viewPager = new ViewPager(context);
    	viewPager.setBackgroundColor(Color.BLACK);
        List<AssetFrame> frames = FrameFactory.createFramesFromAsset(context, getSerializableExtra(String.class), -1);
        
        viewPager.setAdapter(new SamePagerAdapter<AssetFrame>(frames) {
			@Override
			public View getView(AssetFrame t, int position, View convertView) {
				if(convertView == null){
					convertView = new PhotoView(context);
					convertView.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							backStack();
						}
					});
				}
				((ImageView)convertView).setImageDrawable(t.decodeDrawable(context));
				return convertView;
			}
			@Override
			protected void onItemDestroyed(View view, AssetFrame t) {
				super.onItemDestroyed(view, t);
				t.recycle();
			}
		});
        Integer position = getSerializableExtra(Integer.class);
        if(position!=null){
        	viewPager.setCurrentItem(position);
        }
        return viewPager;
    }
}
