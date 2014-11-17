package com.fax.faw_vw.more;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.brand.BrandInnovationFragment;
import com.fax.faw_vw.brand.BrandShowFragment;
import com.fax.faw_vw.findcar.FinancialServiceFragment;
import com.fax.faw_vw.fragment_360.Show360FrameFragment;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_car.MarketFragment;
import com.fax.faw_vw.fragments_car.NewsFragment;
import com.fax.faw_vw.fragments_car.OnlineOrderCarFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.game.OnlineDriveGamePreStartFrag;
import com.fax.faw_vw.menu.QRFragment;
import com.fax.faw_vw.model.ImageTextPagePair;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

//快捷菜单
public class MenuListSettingFragment extends MyFragment {
	private static final int MaxEnableParis = 7;
	static ImageTextPagePair personPair = new ImageTextPagePair(
			R.drawable.main_menu_ic_person, "个人中心", PersonFragment.class);
	static ImageTextPagePair scanQRPair = new ImageTextPagePair(
			R.drawable.main_menu_ic_2wei, "扫描二维码", QRFragment.class);
	
	public final static ImageTextPagePair[] allPagePairs = new ImageTextPagePair[]{
		new ImageTextPagePair(R.drawable.main_menu_ic_360, "360展示", MyApp.createFragment(ShowCarsFragment.class, Show360FrameFragment.class)),
		new ImageTextPagePair(R.drawable.main_menu_ic_order_drive, "预约试驾", BookDriveFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_online_game, "在线试驾", OnlineDriveGamePreStartFrag.class, true),
		new ImageTextPagePair(R.drawable.main_menu_ic_search_dealer, "经销商查询", SearchDealerFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_financial_service, "金融服务", FinancialServiceFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_market, "市场活动", MarketFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_news_voice, "媒体声音", NewsFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_brand_show, "品牌展示", BrandShowFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_innovation, "科技创新", BrandInnovationFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_online_order, "在线订车", OnlineOrderCarFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_query_illegal, "违章查询", QueryIllegalFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_feedback, "意见反馈", FeedbackFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_online_qa, "在线客服", OnlineQAFragment.class),
		new ImageTextPagePair(R.drawable.main_menu_ic_setting, "系统设置", SettingFragment.class),
	};
	static List<String> defaultPairs = Arrays.asList("预约试驾","经销商查询","违章查询","意见反馈","在线客服");
	public static List<ImageTextPagePair> getEnablePagePairs(Context context){
		ArrayList<ImageTextPagePair> enablePairs = new ArrayList<ImageTextPagePair>();
		enablePairs.add(personPair);
		SharedPreferences sp = SettingFragment.getSettingSP(context);
		
		if(defaultPairs!=null){
			for(String defaultPair:defaultPairs){
				Editor editor = sp.edit();
				if(!sp.contains(defaultPair)){
					editor.putBoolean(defaultPair, true);
				}
				editor.commit();
			}
			defaultPairs = null;
		}
		
		for(ImageTextPagePair pagePair : allPagePairs){
			if(sp.getBoolean(pagePair.getText(), false)){
				enablePairs.add(pagePair);
			}
		}
		
		enablePairs.add(scanQRPair);
		return enablePairs;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final SharedPreferences sp = SettingFragment.getSettingSP(context);
		ObjectXListView listView = new ObjectXListView(context);
		
		listView.setAdapter(new ObjectXAdapter.SingleLocalPageAdapter<ImageTextPagePair>() {
			@Override
			public View bindView(final ImageTextPagePair t, int position, View convertView) {
				CheckBox checkBox = (CheckBox) convertView;
				if(checkBox==null){
					checkBox = new CheckBox(context){
						@Override
						public void setButtonDrawable(Drawable d) {
							super.setButtonDrawable(null);
							setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
						}
					};
					int padding = (int) MyApp.convertToDp(12);
					checkBox.setPadding(padding, padding, padding, padding);
				}
				
				checkBox.setText(t.getText());
				checkBox.setOnCheckedChangeListener(null);
				checkBox.setChecked(sp.getBoolean(t.getText(), false));
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked && getEnablePagePairs(context).size()>=MaxEnableParis){
							new AlertDialog.Builder(context).setTitle(android.R.string.dialog_alert_title)
									.setMessage("可选择的条目已达上限，请先取消一个条目")
									.setPositiveButton(android.R.string.ok, null).show();
							buttonView.setChecked(false);
							return;
						}
						sp.edit().putBoolean(t.getText(), isChecked).apply();
					}
				});
				return checkBox;
			}
			@Override
			public List<ImageTextPagePair> instanceNewList() throws Exception {
				return Arrays.asList(allPagePairs);
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("快捷菜单").setContentView(listView);
	}

}
