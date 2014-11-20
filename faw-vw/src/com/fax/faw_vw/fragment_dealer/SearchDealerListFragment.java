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

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.Dealer;
import com.fax.faw_vw.more.QueryIllegalInfoFragment;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

/**经销商列表 */
public class SearchDealerListFragment extends MyFragment {


	private ArrayList<Dealer> dealers;
    /** 选择处理器 */
    public OnSelectItem onSelectItem;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.search_dealer_list, container, false);
		ObjectXListView listView = (ObjectXListView) view.findViewById(android.R.id.list);
		listView.setPullRefreshEnable(false);
		listView.setAdapter(new ObjectXAdapter.SingleLocalPageAdapter<Dealer>() {
			@Override
			public View bindView(Dealer dealer, int position, View view) {
				if(view == null){
					view = View.inflate(context, R.layout.search_dealer_list_item, null);
				}
				LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.brand_dealer_layout);
				nameLayout.removeAllViews();
				if(dealers.size()!=0){
					TextView brandCnTv = new TextView(context);
					brandCnTv.setTypeface(Typeface.DEFAULT_BOLD);
					brandCnTv.setTextSize(18);
					TextView brandEnTv = new TextView(context);
					brandEnTv.setTextSize(13);
					brandEnTv.setTextColor(getResources().getColor(android.R.color.darker_gray));
					brandCnTv.setText(dealer.getFullname());
					brandEnTv.setText(dealer.getSnippet());
					nameLayout.addView(brandCnTv);
					nameLayout.addView(brandEnTv);
				}
				return view;
			}
			@Override
			public void onItemClick(Dealer d, View view, int position, long id) {
				super.onItemClick(d, view, position, id);
			   if (onSelectItem != null) {
                    backStack();
                    onSelectItem.onSelectCar(d);
                } else {
                	SearchDealerDetailFragment searchDealerDetailFragment=new  SearchDealerDetailFragment();
                	searchDealerDetailFragment.onActivityResult(1, Activity.RESULT_OK, MyApp.createIntent(d));
					addFragment(searchDealerDetailFragment);
                }
			}
			@Override
			public List<Dealer> instanceNewList() throws Exception {
				return dealers;
			}
		});

		return new MyTopBar(getActivity()).setLeftBack()
                .setTitle("经销商").setContentView(view);
	}
    public interface OnSelectItem {
        public void onSelectCar(Dealer dealer);
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			dealers =  (ArrayList<Dealer>) data.getSerializableExtra("list");
		}
	}
}



