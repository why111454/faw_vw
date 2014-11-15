package com.fax.faw_vw.findcar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.FitWidthImageView;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.SimpleExpandAdapter;

//金融产品详情
public class FinancialServiceProductDetailFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.financial_service_product_detail, null);
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		final ExpandableListView listView = (ExpandableListView) view.findViewById(android.R.id.list);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
				if(!checkedBtn.isChecked()) return;//avoid check off callback
				Map<String, List<Integer>> map = new LinkedHashMap<String, List<Integer>>();
				switch(checkedId){
				case R.id.radioButton1:
					map.put("标准信贷", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chedai_1
					}));
					map.put("弹性信贷", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chedai_2
					}));
					map.put("阶梯信贷", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chedai_3
					}));
					map.put("缓冲信贷", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chedai_4
					}));
					map.put("信用卡", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chedai_5
					}));
					break;
				case R.id.radioButton2:
					map.put("交强险", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chexian_1
					}));
					map.put("商业险", Arrays.asList(new Integer[]{
							R.drawable.financial_service_product_detail_chexian_2
					}));
					break;
				}
				
				listView.setAdapter(new SimpleExpandAdapter<String, Integer>(map) {
					@Override
					public View getGroupView(int groupPosition,boolean isExpanded, View convertView,String group) {
						if(convertView == null){
							convertView = View.inflate(context, R.layout.car_specs_list_group, null);
						}
						((TextView)convertView).setText(group);
						((TextView)convertView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 
								isExpanded? R.drawable.car_specs_list_group_ic_close : R.drawable.car_specs_list_group_ic_open, 0);
						return convertView;
					}
					@Override
					public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
							View convertView, Integer child) {
						FitWidthImageView imageView = (FitWidthImageView) convertView;
						if(imageView==null){
							imageView=new FitWidthImageView(context);
						}
						imageView.setImageResource(child);
						return imageView;
					}
				});
			}
		});
		((CompoundButton)radioGroup.findViewById(R.id.radioButton1)).setChecked(true);
		
		return new MyTopBar(context).setLeftBack().setTitle("金融产品详情").setContentView(view);
	}

}
