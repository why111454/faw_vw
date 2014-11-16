package com.fax.faw_vw.fragments_main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;

//系统设置
public class SettingFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return new MyTopBar(context).setLeftBack().setTitle("系统设置").setContentView(R.layout.more_setting);
	}

}
