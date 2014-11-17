package com.fax.faw_vw.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;

/**TODO 增值服务页面 */
public class IncrementServiceFragment extends MyFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.more_increment_service, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
				.setTitle("增值服务").setContentView(view);
		
		//TODO 数据绑定、提交
		return topBar;
	}


}
