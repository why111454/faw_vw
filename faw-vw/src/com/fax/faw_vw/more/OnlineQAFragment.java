package com.fax.faw_vw.more;

import java.io.Serializable;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.fargment_common.WebViewFragment;

//在线客服
public class OnlineQAFragment extends WebViewFragment {

	@Override
	public <T extends Serializable> T getSerializableExtra(Class<T> c) {
		if(String.class == c){
			return (T) "http://kf02.faw-vw-dns.com/new/client.php?arg=faw-vw&style=1&m=Mobile";//返回在线客服的地址
		}
		return super.getSerializableExtra(c);
	}

}
