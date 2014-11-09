package com.fax.faw_vw.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;

//意见反馈
public class FeedbackFragment extends MyFragment{
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
					.setTitle("意见反馈").setContentView(R.layout.app_feedback);
			//TODO 数据绑定、提交
			return topBar;
		}
}
