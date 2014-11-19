package com.fax.faw_vw.findcar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.findcar.BuyCarCalculatorHelper.Item;
import com.fax.faw_vw.findcar.BuyCarCalculatorHelper.ItemGroup;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_car.ModelListFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.model.CarModelList.CarModel;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.MultiFormatTextView;
import com.fax.utils.view.list.ExpandListHeadContain;
import com.fax.utils.view.list.SimpleExpandAdapter;

public class BuyCarCalculatorFragment extends MyFragment {
	ShowCarItem showCarItem;
	CarModel carModel;
	
	TextView priceTv;
	ExpandableListView listView;
	MultiFormatTextView headTextView;
	BuyCarCalculatorHelper calculatorHelper = new BuyCarCalculatorHelper();
	int choosedPage;
	SimpleExpandAdapter.MapAdapter<String, BuyCarCalculatorHelper.Item> mapAdapter = new SimpleExpandAdapter.MapAdapter<String, BuyCarCalculatorHelper.Item>() {
		@Override
		public int getGroupCount() {
			return getCurrentPage().getPagePart().size();
		}
		@Override
		public int getChildrenCount(int groupPosition) {
			return getCurrentPage().getPagePart().get(getGroup(groupPosition)).size();
		}
		@Override
		public String getGroup(int groupPosition) {
            return new ArrayList<String>(getCurrentPage().getPagePart().keySet()).get(groupPosition);
		}
		@Override
		public BuyCarCalculatorHelper.Item getChild(int groupPosition, int childPosition) {
            return getCurrentPage().getPagePart().get(getGroup(groupPosition)).get(childPosition);
		}
	};
	private BuyCarCalculatorHelper.Page getCurrentPage(){
		return calculatorHelper.getPages()[choosedPage];
	}
	private void refreshList(boolean force){
		headTextView.setTextMulti(getCurrentPage().getPageTitleFormat()+"\n//S12//#999999此结果仅供参考，实际费用以当地缴费为准");
		priceTv.setText("购车价格:"+calculatorHelper.getCarPrice()+"");

		if(force){
			listView.setAdapter(new SimpleExpandAdapter<String, BuyCarCalculatorHelper.Item>(mapAdapter) {
				@Override
				public View getGroupView(int groupPosition, boolean isExpanded, View convertView, String group) {
					if(convertView==null){
						convertView = View.inflate(context, R.layout.financial_buy_car_list_group, null);
						convertView.setClickable(true);//禁止收缩
					}
					((TextView)convertView.findViewById(android.R.id.text1)).setText(group);
					ItemGroup itemGroup = getCurrentPage().getPagePart().get(group);
					((TextView)convertView.findViewById(android.R.id.text2))
							.setText(itemGroup.isSum()?itemGroup.getSum()+"":"");
					return convertView;
				}
				@Override
				public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
						Item child) {
					if(convertView==null){
						convertView = View.inflate(context, R.layout.common_title_summary_item, null);
					}
					((TextView)convertView.findViewById(android.R.id.title)).setText(child.getTitle());
					TextView summaryTv = ((TextView)convertView.findViewById(android.R.id.summary));
					summaryTv.setText(child.getSummary());
					if(summaryTv.getText().length()==0){
						summaryTv.setVisibility(View.GONE);
					}else{
						summaryTv.setVisibility(View.VISIBLE);
					}
					((TextView)convertView.findViewById(android.R.id.content)).setText(child.getValue()+"");
					return convertView;
				}
			});
		}
		((BaseExpandableListAdapter)listView.getExpandableListAdapter()).notifyDataSetChanged();
		((ExpandListHeadContain)listView.getParent()).refreshGroupHead();
		for(int i=0;i<mapAdapter.getGroupCount();i++){
			listView.expandGroup(i);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(context, R.layout.financial_buy_car_calculator, null);
		listView = (ExpandableListView) view.findViewById(android.R.id.list);
		headTextView = new MultiFormatTextView(context);
		headTextView.setTextSize(16);
		int pad6 = (int) MyApp.convertToDp(6);
		headTextView.setPadding(pad6, pad6, pad6, pad6);
		headTextView.setGravity(Gravity.CENTER);
		listView.addHeaderView(headTextView);
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				Item child = mapAdapter.getChild(groupPosition, childPosition);
				child.showChooseValue(context, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						refreshList(false);
					}
					
				});
				return true;
			}
		});
		listView.setGroupIndicator(null);
		priceTv = (TextView) view.findViewById(R.id.select_car_price_button);
		priceTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText editText = new EditText(context);
				editText.setInputType(InputType.TYPE_CLASS_NUMBER);
				editText.setText(calculatorHelper.getCarPrice()+"");
				new AlertDialog.Builder(context).setTitle("实际成交价格")
						.setView(editText)
						.setNegativeButton(android.R.string.cancel, null)
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try {
									int carPrice = Integer.valueOf(editText.getText().toString());
									calculatorHelper.setCarPrice(carPrice);
									refreshList(false);
								} catch (Exception e) {
									Toast.makeText(context, "价格解析出错", Toast.LENGTH_SHORT).show();
								}
							}
						}).show();
			}
		});
		
		RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton rb = (CompoundButton)group.findViewById(checkedId);
				if(rb==null || !rb.isChecked()) return;
				choosedPage = group.indexOfChild(rb);
				if(choosedPage<0){
					choosedPage = 0;
				}
				refreshList(true);
			}
		});

		view.findViewById(R.id.select_car_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
                ShowCarsFragment showCarsFragment = new ShowCarsFragment();
                showCarsFragment.onSelectItem = new ShowCarsFragment.OnSelectItem() {
					@Override
					public void onSelectCar(ShowCarItem item) {
						showCarItem = item;
						((TextView)v).setText(item.getModel_cn());
					}
				};
                addFragment(showCarsFragment);
			}
		});
		view.findViewById(R.id.select_car_model_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
                if (showCarItem == null) {
                    Toast.makeText(context, "请先选择车型", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModelListFragment modelListFragment = ModelListFragment.newInstance(showCarItem, null);
                modelListFragment.onSelectItem = new ModelListFragment.OnSelectItem() {
					@Override
					public void onSelectCarModel(CarModel item) {
						carModel = item;
						((TextView)v).setText(item.getModel_name());
						calculatorHelper.setCarModel(carModel);
						refreshList(false);
					}
				};
                addFragment(modelListFragment);
			}
		});
		
		refreshList(true);
		return new MyTopBar(context).setLeftBack().setTitle("购车计算器")
				.setContentView(view);
	}

}
