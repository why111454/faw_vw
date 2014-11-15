package com.fax.faw_vw.fragments_car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.PersonalizedItem;
import com.fax.faw_vw.views.MyTopBar;

//个性化选车
public class PersonalizedChooseCar extends MyFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.personalized_choose_car, container, false);
		final ListView listView = (ListView) view.findViewById(android.R.id.list);
		listView.setAdapter(new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView == null){
					convertView = new TextView(context);
					int padding = (int) MyApp.convertToDp(10);
					convertView.setPadding(0, padding, 0, padding);
					((TextView) convertView).setTextColor(getResources().getColor(android.R.color.darker_gray));
				}
				((TextView) convertView).setText(getItem(position).getQuestion());
				return convertView;
			}
			@Override
			public long getItemId(int position) {
				return 0;
			}
			@Override
			public PersonalizedItem getItem(int position) {
				return PersonalizedItem.Items[position];
			}
			@Override
			public int getCount() {
				return PersonalizedItem.Items.length;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PersonalizedItem item = (PersonalizedItem) listView.getItemAtPosition(position);
				addFragment(MyApp.createFragment(PersonalizedChooseCarDetail.class, item));
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("个性化选车")
				.setContentView(view);
	}

}
