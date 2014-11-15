package com.fax.faw_vw.fragments_car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.CarModelList.CarModel;
import com.fax.faw_vw.model.Specs;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.task.GsonAsyncTask;
import com.fax.utils.view.list.ExpandListHeadContain;

public class CarSpecsFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		CarModel model = (CarModel) getArguments().getSerializable(CarModel.class.getName());
		final ExpandableListView listView = new ExpandableListView(context);
		new GsonAsyncTask<Specs>(context, MyApp.getApiSpecsUrl(model)) {
			@Override
			protected void onPostExecuteSuc(final Specs specs) {
				final BaseExpandableListAdapter adapter = (new BaseExpandableListAdapter() {
					@Override
					public boolean isChildSelectable(int groupPosition, int childPosition) {
						return false;
					}
					@Override
					public boolean hasStableIds() {
						return false;
					}
					
					@Override
					public View getGroupView(int groupPosition,boolean isExpanded, View convertView,final ViewGroup parent) {
						if(convertView == null){
							convertView = View.inflate(context, R.layout.car_specs_list_group, null);
						}
						((TextView)convertView).setText(getGroup(groupPosition));
						((TextView)convertView).setCompoundDrawablesWithIntrinsicBounds(0, 0, 
								isExpanded? R.drawable.car_specs_list_group_ic_close : R.drawable.car_specs_list_group_ic_open, 0);
						return convertView;
					}
					@Override
					public long getGroupId(int groupPosition) {
						return 0;
					}
					@Override
					public int getGroupCount() {
						return specs.getGroupCount();
					}
					@Override
					public String getGroup(int groupPosition) {
						return specs.getGroup(groupPosition);
					}
					@Override
					public int getChildrenCount(int groupPosition) {
						return specs.getChildrenCount(groupPosition);
					}
					@Override
					public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
							ViewGroup parent) {
						if(convertView == null) convertView = View.inflate(context, R.layout.car_specs_list_item, null);
						Specs.Item item = getChild(groupPosition, childPosition);
						((TextView)convertView.findViewById(android.R.id.title)).setText(item.getTitle());
						((TextView)convertView.findViewById(android.R.id.summary)).setText(item.getValue());
						return convertView;
					}
					@Override
					public long getChildId(int groupPosition, int childPosition) {
						return 0;
					}
					@Override
					public Specs.Item getChild(int groupPosition, int childPosition) {
						return specs.getChild(groupPosition, childPosition);
					}
				});
				listView.setAdapter(adapter);
				listView.setGroupIndicator(null);
				for(int i=0;i<specs.getGroupCount();i++){
					listView.expandGroup(i);
				}
				
//				listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//					@Override
//					public void onScrollStateChanged(AbsListView view, int scrollState) {
//					}
//					int showingGroupPositon = -1;
//					@Override
//					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//						try {
//							if (view.getItemAtPosition(firstVisibleItem + 1) instanceof String
//									&& view.getChildAt(1).getTop() < groupHead.getHeight()) {
//								((MarginLayoutParams) groupHead.getLayoutParams()).topMargin = view.getChildAt(1)
//										.getTop() - groupHead.getHeight();
//							} else
//								((MarginLayoutParams) groupHead.getLayoutParams()).topMargin = 0;
//							groupHead.requestLayout();
//						} catch (Exception e) {
//						}
//						int groupPositon = ExpandableListView.getPackedPositionGroup(listView.getExpandableListPosition(firstVisibleItem));
//						
//						if(showingGroupPositon != groupPositon){
//							showingGroupPositon = groupPositon;
//							adapter.getGroupView(showingGroupPositon, listView.isGroupExpanded(showingGroupPositon), groupHead, view);
//							groupHead.setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									if(listView.isGroupExpanded(showingGroupPositon)){
//										listView.collapseGroup(showingGroupPositon);
//										if(showingGroupPositon<adapter.getGroupCount()-1) 
//											listView.setSelectedGroup(showingGroupPositon+1);
//									}
//									else listView.expandGroup(showingGroupPositon);
//									
//									adapter.getGroupView(showingGroupPositon, listView.isGroupExpanded(showingGroupPositon), groupHead, listView);
//								}
//							});
//						}
//					}
//				});
				
			}
		}.setProgressDialog().execute();
		return new MyTopBar(getActivity()).setLeftBack()
				.setTitle(model.getModel_name())
				.setContentView(new ExpandListHeadContain(context, listView));
	}

}
