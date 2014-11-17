package com.fax.faw_vw.more;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;

//系统设置
public class SettingFragment extends MyFragment {
	public static SharedPreferences getSettingSP(Context context){
		return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(context, R.layout.more_setting, null);
		view.findViewById(R.id.more_setting_menu_list).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addFragment(MyApp.createFragment(MenuListSettingFragment.class));
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("系统设置").setContentView(view);
	}

}
