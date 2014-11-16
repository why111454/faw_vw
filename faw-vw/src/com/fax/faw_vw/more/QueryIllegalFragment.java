package com.fax.faw_vw.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.views.MyTopBar;

/**TODO 违章查询页面 */
public class QueryIllegalFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new MyTopBar(context).setLeftBack().setTitle("违章查询");
	}

}
