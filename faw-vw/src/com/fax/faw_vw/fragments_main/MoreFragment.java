package com.fax.faw_vw.fragments_main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragments_car.CarDownloadFragment;
import com.fax.faw_vw.model.ImageTextPagePair;
import com.fax.faw_vw.more.FeedbackFragment;
import com.fax.faw_vw.more.OnlineQAFragment;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.faw_vw.views.clickshow.ClickShowTextView;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
/**更多 页卡 */
public class MoreFragment extends MyFragment {
	ImageTextPagePair[] pagePairs = new ImageTextPagePair[]{
//			new ImageTextPagePair(R.drawable.main_more_aftermarket, "售后服务", null),
			new ImageTextPagePair(R.drawable.main_more_person, "个人中心", null),
			new ImageTextPagePair(R.drawable.main_more_query_illegal, "违章查询", null),
			new ImageTextPagePair(R.drawable.main_more_extra, "增值服务", null),
			new ImageTextPagePair(R.drawable.main_more_online_service, "在线客服", MyApp.createFragment(OnlineQAFragment.class)),
			new ImageTextPagePair(R.drawable.main_more_feedback, "意见反馈", MyApp.createFragment(FeedbackFragment.class)),
			new ImageTextPagePair(R.drawable.main_more_setting, "系统设置", null),
			new ImageTextPagePair(R.drawable.main_more_downcenter, "下载中心", MyApp.createFragment(ShowCarsFragment.class, CarDownloadFragment.class)),	
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ObjectXListView listView = new ObjectXListView(context);
		listView.setPullRefreshEnable(false);
		listView.setDrawSelectorOnTop(true);
		listView.setBackgroundColor(Color.WHITE);
		listView.setSelector(R.drawable.common_btn_in_white);
		listView.setDivider(getResources().getDrawable(android.R.color.darker_gray));
		listView.setAdapter(new ObjectXAdapter.SingleLocalGridPageAdapter<ImageTextPagePair>(2) {
			@Override
			public List<ImageTextPagePair> instanceNewList() throws Exception {
				return Arrays.asList(pagePairs);
			}
			@Override
			protected View bindGridView(ViewGroup contain,final ImageTextPagePair t,final int position, View convertView) {
				TextView tv = (TextView) convertView;
				if(tv==null){
					int padding = (int) MyApp.convertToDp(20);
					tv = new TextView(context);
					tv.setTextSize(15);
					tv.setCompoundDrawablePadding(padding/2);
					tv.setGravity(Gravity.CENTER);
					tv.setPadding(padding, padding, padding, padding);
				}
				tv.setText(t.getText());
				tv.setCompoundDrawablesWithIntrinsicBounds(0, t.getImgResId(), 0, 0);
				return tv;
			}
			@Override
			public void onItemClick(ImageTextPagePair t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);
				if(t.getFragment()==null) return;
				FragmentContain.start(getActivity(), t.getFragment());
			}
			@Override
			protected int getDividerHeight() {
				return 1;
			}
		});

		return new MyTopBar(context).setTitle("更多").setContentView(listView);
	}
}
