package com.fax.faw_vw.fragments_main;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.findcar.FinancialServiceCoreFragment;
import com.fax.faw_vw.findcar.FinancialServiceFragment;
import com.fax.faw_vw.findcar.FinancialServiceProductFragment;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_car.MarketFragment;
import com.fax.faw_vw.fragments_car.NewsFragment;
import com.fax.faw_vw.fragments_car.OnlineOrderCarFragment;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**寻车助手 页卡 */
public class FindCarsAssistorFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_findcar, container, false);
		view.findViewById(R.id.main_findcar_market).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//市场活动
				FragmentContain.start(getActivity(), MarketFragment.class);
			}
		});
		view.findViewById(R.id.main_findcar_news).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//媒体声音
				FragmentContain.start(getActivity(), NewsFragment.class);
			}
		});
		view.findViewById(R.id.main_findcar_financial).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//金融服务
				FragmentContain.start(getActivity(), FinancialServiceFragment.class);
			}
		});
		view.findViewById(R.id.main_findcar_online_order).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//在线订车
				FragmentContain.start(getActivity(), OnlineOrderCarFragment.class);
			}
		});
		view.findViewById(R.id.main_findcar_dealer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//购车计算器
				FragmentContain.start(getActivity(), SearchDealerFragment.class);
			}
		});
		return new MyTopBar(context).setTitle("寻车助手").setContentView(view);
	}
}
