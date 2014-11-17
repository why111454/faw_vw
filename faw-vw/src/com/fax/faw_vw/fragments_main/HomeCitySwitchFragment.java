package com.fax.faw_vw.fragments_main;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ExpandListHeadContain;
import com.fax.utils.view.list.SimpleExpandAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HomeCitySwitchFragment extends MyFragment {
	public static final String Extra_City = "city";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ExpandableListView listView = new ExpandableListView(context);
		listView.setGroupIndicator(null);
		try {
			String cityDictJson = IOUtils.toString(context.getAssets().open("citydict.json"));
			LinkedHashMap<String, List<String>> map = new Gson().fromJson(cityDictJson,
					new TypeToken<LinkedHashMap<String, List<String>>>() { }.getType());
			listView.setAdapter(new SimpleExpandAdapter<String, String>(map) {
	            @Override
	            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, String group) {
	                if(convertView==null){
	                    convertView= View.inflate(context, R.layout.common_title_item, null);
	                    convertView.setBackgroundResource(R.color.window_bg);
	                }
	                TextView title = ((TextView)convertView.findViewById(android.R.id.title));
	                title.setText(group);
//	                title.setCompoundDrawablesWithIntrinsicBounds(0, 0,
//	                        isExpanded?R.drawable.common_ic_arrow_up:R.drawable.common_ic_arrow_down, 0);
	                return convertView;
	            }

	            @Override
	            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
						final String child) {
	                if(convertView==null){
	                    convertView= View.inflate(context, R.layout.common_title_item, null);
	                    convertView.setPadding((int) MyApp.convertToDp(10), 0 , 0, 0);
	                }
	                ((TextView)convertView.findViewById(android.R.id.title)).setText(child);
	                convertView.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                    	Intent resultDate = new Intent().putExtra(Extra_City, child);
	                    	if(getTargetFragment()!=null){
	                    		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultDate);
	                    	}else{
	                    		getActivity().setResult(Activity.RESULT_OK, resultDate);
	                    	}
	                        backStack();
	                    }
	                });
	                return convertView;
	            }
			});
			
		} catch (Exception e) {
		}
		return new MyTopBar(context).setLeftBack().setTitle("城市切换")
				.setContentView(new ExpandListHeadContain(context, listView));
	}

}
