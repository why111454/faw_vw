package com.fax.faw_vw.findcar;

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

//金融产品
public class FinancialServiceProductFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.financial_service_product, null);
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		final ImageView midImage = (ImageView) view.findViewById(R.id.financial_service_product_content);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
				if(!checkedBtn.isChecked()) return;//avoid check off callback
				switch(checkedId){
				case R.id.radioButton1:
					midImage.setImageResource(R.drawable.financial_service_product_1);
					break;
				case R.id.radioButton2:
					midImage.setImageResource(R.drawable.financial_service_product_2);
					break;
				case R.id.radioButton3:
					midImage.setImageResource(R.drawable.financial_service_product_3);
					break;
				case R.id.radioButton4:
					midImage.setImageResource(R.drawable.financial_service_product_4);
					break;
				case R.id.radioButton5:
					midImage.setImageResource(R.drawable.financial_service_product_5);
					break;
				case R.id.radioButton6:
					midImage.setImageResource(R.drawable.financial_service_product_6);
					break;
				}
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("金融产品").setContentView(view);
	}

}
