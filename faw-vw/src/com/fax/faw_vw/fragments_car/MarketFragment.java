package com.fax.faw_vw.fragments_car;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fargment_common.LocalWebViewFragment;
import com.fax.faw_vw.model.MarketNewsModelList;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;
import com.google.gson.Gson;

import java.util.List;

/**
 * 市场活动
 */
public class MarketFragment extends MyFragment {
    int sourcetype;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = new MyTopBar(context).setLeftBack()
        		.setTitle("市场活动").setContentView(R.layout.market);
        final ObjectXListView listView = (ObjectXListView) view.findViewById(android.R.id.list);
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerHeight((int) MyApp.convertToDp(8));
        
        listView.setAdapter(new ObjectXAdapter.PagesAdapter<MarketNewsModelList.NewsModel>() {
            @Override
            public String getUrl(int page) {
                return "http://faw-vw.allyes.com/index.php?g=api&m=apicache&a=getnews&newsbigtype=0&sourcetype="+sourcetype
                		+"&page="+page+"&pagesize=10";
            }
            @Override
            public View bindView(MarketNewsModelList.NewsModel t, int position, View convertView) {
                if(convertView== null){
                	convertView = View.inflate(context, R.layout.market_news_list_item, null);
                }
//                BitmapManager.bindView(convertView.findViewById(android.R.id.icon), t.getTHUM_PICTURE());
                ((TextView)convertView.findViewById(android.R.id.title)).setText(t.getTITLE());
                ((TextView)convertView.findViewById(android.R.id.summary)).setText(t.getUPDATE_TIME());
                return convertView;
            }
            @Override
            public List<MarketNewsModelList.NewsModel> instanceNewList(String json) throws Exception {
                return new Gson().fromJson(json, MarketNewsModelList.class).getModels();//用gson实例化json数据
            }
            @Override
            public void onItemClick(MarketNewsModelList.NewsModel t, View view, int position, long id) {
                super.onItemClick(t, view, position, id);
                LocalWebViewFragment.start(getActivity(), t.getCONTENT(), t.getTITLE());
            }
        });
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				CompoundButton checkedBtn = (CompoundButton)group.findViewById(checkedId);
				if(!checkedBtn.isChecked()) return;//avoid check off callback
				switch(checkedId){
				case R.id.radioButton1:
					sourcetype = 0;
					break;
				case R.id.radioButton2:
					sourcetype = 1;
					break;
				case R.id.radioButton3:
					sourcetype = 2;
					break;
				}
				listView.reload();
			}
		});
		((CompoundButton)radioGroup.findViewById(R.id.radioButton1)).setChecked(true);

        return view;
    }


}
