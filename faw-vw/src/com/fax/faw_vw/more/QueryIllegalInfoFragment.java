package com.fax.faw_vw.more;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.IllegalInfo;
import com.fax.faw_vw.views.MyTopBar;

/**TODO 违章信息页面 */
public class QueryIllegalInfoFragment extends MyFragment {
	private IllegalInfo illegal;
	TextView date;
	TextView area;
	TextView act;
	TextView money;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.more_query_illegal_info, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context).setLeftBack()
				.setTitle("违章查询").setContentView(view);
		 ((TextView)view.findViewById(R.id.date)).setText(illegal.getResult().getLists().get(0).getDate());
		 ((TextView)view.findViewById(R.id.area)).setText(illegal.getResult().getLists().get(0).getArea());
		 ((TextView)view.findViewById(R.id.act)).setText(illegal.getResult().getLists().get(0).getAct());
		 ((TextView)view.findViewById(R.id.money)).setText(illegal.getResult().getLists().get(0).getMoney());
		//TODO 数据绑定、提交
		return topBar;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			illegal =  (IllegalInfo) data.getSerializableExtra(IllegalInfo.class.getName());
		}
	}
}
