package com.fax.faw_vw.fragment_dealer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.model.Dealer;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

/**经销商详情页 */
public class SearchDealerDetailFragment extends MyFragment {
	public static final int Request_SelectDealer = 1;
	TextView dealer_fullname;
	TextView dealer_address;
	TextView dealer_selltel;
	TextView money;
	private Dealer dealer;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.search_dealer_detail, container, false);
        ((TextView)view.findViewById(R.id.dealer_fullname)).setText(dealer.getFullname());
        ((TextView)view.findViewById(R.id.dealer_address)).setText(dealer.getAddress());
        ((TextView)view.findViewById(R.id.dealer_selltel)).setText(dealer.getSelltel());
        view.findViewById(R.id.home_order_drive_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(BookDriveFragment.class, dealer));
			}
		});
        
		 return new MyTopBar(getActivity()).setLeftBack()
	                .setTitle("预约试驾").setContentView(view);
	}
   
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			dealer =  (Dealer) data.getSerializableExtra(Dealer.class.getName());
		}
	}
}



