package com.fax.faw_vw.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.views.MyTopBar;

/**个人中心 */
public class PersonFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return new MyTopBar(context).setLeftBack().setTitle("个人中心");
	}

}
