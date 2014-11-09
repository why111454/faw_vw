package com.fax.faw_vw.fragments_car;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.CarModelList.CarModel;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.model.Specs;
import com.fax.faw_vw.model.Specs.CompareItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.task.GsonAsyncTask;
import com.fax.utils.view.list.ExpandListHeadContain;

//配置对比
public class CarSpecsCompareFragment extends MyFragment{
	ExpandableListView listView;
	ShowCarItem carItem;
	RadioGroup radioGroup;
	View lastClickContain;
	View.OnClickListener selectModelClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			lastClickContain = v;
			FragmentContain.start(CarSpecsCompareFragment.this, ModelListFragment.class, MyApp.createIntent(carItem), 1);
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		carItem = (ShowCarItem) getArguments().getSerializable(ShowCarItem.class.getName());
		
		View headView = inflater.inflate(R.layout.car_specs_compare_head, null);
		View modelContain1 = headView.findViewById(R.id.car_specs_compare_item1);
		modelContain1.setOnClickListener(selectModelClickListener);
		View modelContain2 = headView.findViewById(R.id.car_specs_compare_item2);
		modelContain2.setOnClickListener(selectModelClickListener);
		radioGroup = (RadioGroup) headView.findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
		        if(checkedId<=0 || !((CompoundButton)group.findViewById(checkedId)).isChecked()) return;//avoid check off callback
		        refreshList();
			}
		});
		
		listView = new ExpandableListView(context);
		listView.addHeaderView(headView);
		listView.setGroupIndicator(null);
		refreshList();
		return new MyTopBar(context).setLeftBack()
				.setTitle("车型对比").setContentView(
						new ExpandListHeadContain(context, listView));
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			final CarModel carModel = (CarModel) data.getSerializableExtra(CarModel.class.getName());
			if(lastClickContain != null)
				new GsonAsyncTask<Specs>(context, MyApp.getApiSpecsUrl(carModel)) {
					@Override
					protected void onPostExecuteSuc(Specs result) {
						if(lastClickContain.getId() == R.id.car_specs_compare_item1){
							carModel1 = carModel;
							specs1 = result;
						}
						else if(lastClickContain.getId() == R.id.car_specs_compare_item2){
							carModel2 = carModel;
							specs2 = result;
						}
						((TextView)lastClickContain.findViewById(R.id.model_name)).setText(carModel.getModel_name());
						((TextView)lastClickContain.findViewById(R.id.model_price)).setText(carModel.getPrice());
						BitmapManager.bindView(lastClickContain.findViewById(R.id.model_img), carModel.getComparepicUrl());
						CompoundButton rb = (CompoundButton)radioGroup.findViewById(android.R.id.button1);
						if(!rb.isChecked()) rb.setChecked(true);
						else refreshList();
					}
				}.setProgressDialog().execute();
		}
	}
	CarModel carModel1;
	CarModel carModel2;
	Specs specs1;
	Specs specs2;
	BaseExpandableListAdapter adapter;
	private void refreshList(){
		final Specs newSpecs = new Specs(specs1, specs2, radioGroup.getCheckedRadioButtonId());
		listView.setAdapter(new BaseExpandableListAdapter() {
			@Override
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				return false;
			}
			@Override
			public boolean hasStableIds() {
				return false;
			}
			@Override
			public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
				if(convertView==null) convertView=View.inflate(context, R.layout.car_specs_list_compare_group, null);
				((TextView)convertView.findViewById(R.id.car_specs_compare_group_name)).setText(getGroup(groupPosition));
				((TextView)convertView.findViewById(R.id.car_specs_compare_group_name))
					.setCompoundDrawablesWithIntrinsicBounds(
							isExpanded? R.drawable.car_specs_list_group_ic_close : R.drawable.car_specs_list_group_ic_open,0, 0,  0);
				
				((TextView)convertView.findViewById(R.id.car_specs_compare_group_model1_name))
					.setText(carModel1==null?null:carModel1.getModel_name());
				((TextView)convertView.findViewById(R.id.car_specs_compare_group_model2_name))
					.setText(carModel2==null?null:carModel2.getModel_name());
				return convertView;
			}
			@Override
			public long getGroupId(int groupPosition) {
				return 0;
			}
			@Override
			public int getGroupCount() {
				return newSpecs.getGroupCount();
			}
			@Override
			public String getGroup(int groupPosition) {
				return newSpecs.getGroup(groupPosition);
			}
			@Override
			public int getChildrenCount(int groupPosition) {
				return newSpecs.getChildrenCount(groupPosition);
			}
			@Override
			public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
					ViewGroup parent) {
				if(convertView==null) convertView = View.inflate(context, R.layout.car_specs_list_compare_item, null);
				Specs.CompareItem child = getChild(groupPosition, childPosition);
				((TextView)convertView.findViewById(android.R.id.title)).setText(child.getTitle());
				TextView text1 = ((TextView)convertView.findViewById(android.R.id.text1));
				TextView text2 = ((TextView)convertView.findViewById(android.R.id.text2));
				text1.setText(child.getValue());
				text2.setText(child.getCompareValue());
				if(child.getValue()==null || !child.getValue().equals(child.getCompareValue())){
					text1.setTextColor(Color.RED);
					text2.setTextColor(Color.RED);
				}else{
					text1.setTextColor(Color.BLACK);
					text2.setTextColor(Color.BLACK);
				}
				return convertView;
			}
			@Override
			public long getChildId(int groupPosition, int childPosition) {
				return 0;
			}
			@Override
			public Specs.CompareItem getChild(int groupPosition, int childPosition) {
				return (CompareItem) newSpecs.getChild(groupPosition, childPosition);
			}
		});
	}
	
}
