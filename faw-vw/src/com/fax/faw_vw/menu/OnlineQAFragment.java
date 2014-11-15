package com.fax.faw_vw.menu;

import java.io.Serializable;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.fargment_common.WebViewFragment;

//在线问答
public class OnlineQAFragment extends WebViewFragment {

	@Override
	public <T extends Serializable> T getSerializableExtra(Class<T> c) {
		if(String.class == c){
			return (T) "";//FIXME 返回在线问答的地址
		}
		return super.getSerializableExtra(c);
	}

}
