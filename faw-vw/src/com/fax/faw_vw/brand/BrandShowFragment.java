package com.fax.faw_vw.brand;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.frameAnim.AssetFrame;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameAnimation;
import com.fax.utils.frameAnim.FrameFactory;

//品牌展示
public class BrandShowFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.brand_show, container, false);
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		final ImageView midImage = (ImageView) view.findViewById(R.id.brand_show_content);
		final ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		final List<AssetFrame> frames = FrameFactory.createFramesFromAsset(context, "brand/show", -1);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
				if(!checkedBtn.isChecked()) return;//avoid check off callback
				switch(checkedId){
				case R.id.radioButton1:
					FrameAnimation.setFrameToView(midImage, frames.get(0));
					scrollView.scrollTo(0, 0);
					break;
				case R.id.radioButton2:
					FrameAnimation.setFrameToView(midImage, frames.get(1));
					scrollView.scrollTo(0, 0);
					break;
				}
			}
		});
		((CompoundButton)radioGroup.findViewById(R.id.radioButton1)).setChecked(true);
		
		return new MyTopBar(context).setLeftBack().setTitle("品牌展示").setContentView(view);
	}

}
