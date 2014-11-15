package com.fax.faw_vw.brand;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;

//品牌展示
public class BrandShowFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.brand_show, container, false);
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		final ImageView midImage = (ImageView) view.findViewById(R.id.brand_show_content);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
				if(!checkedBtn.isChecked()) return;//avoid check off callback
				switch(checkedId){
				case R.id.radioButton1:
					midImage.setImageResource(R.drawable.brand_show_1);
					break;
				case R.id.radioButton2:
					midImage.setImageResource(R.drawable.brand_show_2);
					break;
				}
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("品牌展示").setContentView(view);
	}

}
