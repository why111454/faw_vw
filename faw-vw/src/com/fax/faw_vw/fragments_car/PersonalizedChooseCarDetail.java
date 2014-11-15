package com.fax.faw_vw.fragments_car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.PersonalizedItem;
import com.fax.faw_vw.views.MyTopBar;


//个性化选车
public class PersonalizedChooseCarDetail extends MyFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.personalized_choose_car_detail, container, false);
		final PersonalizedItem item = getSerializableExtra(PersonalizedItem.class);
		((TextView)view.findViewById(R.id.choose_car_type)).setText(item.getResultType());
		((TextView)view.findViewById(R.id.choose_car_name)).setText(item.getCarItem().getModel_cn());
		((TextView)view.findViewById(R.id.choose_car_extra)).setText(item.getResultExtra());
		((ImageView)view.findViewById(R.id.choose_car_img)).setImageResource(item.getImgRes());
		view.findViewById(R.id.choose_car_retry).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
			}
		});
		view.findViewById(R.id.choose_car_look_detail).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(CarDetailFragment.class, item.getCarItem()));
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("个性化选车")
				.setContentView(view);
	}

}
