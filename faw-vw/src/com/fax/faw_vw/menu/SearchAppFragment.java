package com.fax.faw_vw.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_car.CarDetailFragment;
import com.fax.faw_vw.fragments_car.CarDownloadFragment;
import com.fax.faw_vw.fragments_car.CarSpecsFragment;
import com.fax.faw_vw.fragments_car.MarketFragment;
import com.fax.faw_vw.fragments_car.ModelListFragment;
import com.fax.faw_vw.fragments_car.NewsFragment;
import com.fax.faw_vw.fragments_car.PersonalizedChooseCar;
import com.fax.faw_vw.fragments_car.RequestPriceFragment;
import com.fax.faw_vw.fragments_main.FindCarsAssistorFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.model.ShowCarItemRes;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

//搜索App里的所有页面，是一个页面索引的搜索，点击结果跳转到不同的页面（后面提供可搜索的关键字和页面）
public class SearchAppFragment extends MyFragment{

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final EditText wordTv = new EditText(context);
		wordTv.setHint("请输入搜索关键字");
		final ObjectXListView listView = new ObjectXListView(context);
		listView.setAdapter(new ObjectXAdapter.SingleLocalPageAdapter<KeyPagePair>(){
			@Override
			public View bindView(KeyPagePair t, int position, View convertView) {
				if(convertView==null) convertView = View.inflate(context, android.R.layout.simple_list_item_1, null);
				((TextView)convertView.findViewById(android.R.id.text1)).setText(t.key);
				return convertView;
			}
			@Override
			public List<KeyPagePair> instanceNewList() throws Exception {
				ArrayList<KeyPagePair> keyPagePairs = new ArrayList<SearchAppFragment.KeyPagePair>();
				for(KeyPagePair keyPagePair : pagePairs){
					if(keyPagePair.key.contains(wordTv.getText().toString())) keyPagePairs.add(keyPagePair);
				}
				return keyPagePairs;
			}
			@Override
			public void onItemClick(KeyPagePair t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);
				FragmentContain.start(getActivity(), t.fragment);
			}
		});
		
		wordTv.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				listView.reload();
			}
		});
		listView.reload();
		return new MyTopBar(context).setLeftBack().setTitle("搜索")
				.addViewInner(wordTv).setContentView(listView);
	}
	
	private static ArrayList<KeyPagePair> pagePairs = new ArrayList<KeyPagePair>();
	static {
		for(ShowCarItem carItem : ShowCarItem.SHOW_CAR_ITEMS){
			pagePairs.add(new KeyPagePair(carItem.getModel_cn(), MyApp.createFragment(CarDetailFragment.class, carItem)));
			pagePairs.add(new KeyPagePair(carItem.getModel_cn()+" 配置表", MyApp.createFragment(ModelListFragment.class, carItem, CarSpecsFragment.class)));
		}
		ShowCarItemRes.CarItemChild[] itemChildren = ShowCarItem.SHOW_CAR_ITEM_SAGITAR.getDetailRes().getItemChildren();
		itemChildren = Arrays.copyOfRange(itemChildren, 1, itemChildren.length);
		for(ShowCarItemRes.CarItemChild itemChild : itemChildren){
			pagePairs.add(new KeyPagePair(itemChild.getName(), MyApp.createFragment(CarDetailFragment.class, ShowCarItem.SHOW_CAR_ITEM_SAGITAR, itemChild)));
			pagePairs.add(new KeyPagePair(itemChild.getName()+" 配置表", MyApp.createFragment(ModelListFragment.class, ShowCarItem.SHOW_CAR_ITEM_SAGITAR)));
		}
		pagePairs.add(new KeyPagePair("产品展示", MyApp.createFragment(ShowCarsFragment.class, CarDetailFragment.class)));
		
		pagePairs.add(new KeyPagePair("预约试驾", MyApp.createFragment(BookDriveFragment.class)));
		pagePairs.add(new KeyPagePair("意见反馈", MyApp.createFragment(FeedbackFragment.class)));
		pagePairs.add(new KeyPagePair("在线问答", MyApp.createFragment(OnlineQAFragment.class)));
		pagePairs.add(new KeyPagePair("市场活动", MyApp.createFragment(MarketFragment.class)));
		pagePairs.add(new KeyPagePair("媒体声音", MyApp.createFragment(NewsFragment.class)));
		pagePairs.add(new KeyPagePair("经销商查询", MyApp.createFragment(SearchDealerFragment.class)));
		pagePairs.add(new KeyPagePair("个性化选车", MyApp.createFragment(PersonalizedChooseCar.class)));
		pagePairs.add(new KeyPagePair("报价索取", MyApp.createFragment(RequestPriceFragment.class)));
		pagePairs.add(new KeyPagePair("下载中心", MyApp.createFragment(ShowCarsFragment.class, CarDownloadFragment.class)));
	}
	
	private static class KeyPagePair implements Serializable{
		String key;
		Fragment fragment;
		public KeyPagePair(String key, Fragment fragment) {
			super();
			this.key = key;
			this.fragment = fragment;
		}
		@Override
		public boolean equals(Object o) {
			if(o instanceof KeyPagePair){
				KeyPagePair another = (KeyPagePair) o;
				if(another!=null && another.key!=null && another.key.equals(key)) return true;
			}
			return super.equals(o);
		}
		@Override
		public int hashCode() {
			if(key!=null) return key.hashCode();
			return super.hashCode();
		}
		
	}

}
