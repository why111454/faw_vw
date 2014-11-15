package com.fax.faw_vw.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class FirstHideSpinnerAdapter extends MySpinnerAdapter{

    public FirstHideSpinnerAdapter(ArrayList list, int showingTextColor) {
		super(list, showingTextColor);
		allLists.add(0, null);
	}
	public FirstHideSpinnerAdapter(ArrayList list) {
		super(list);
		allLists.add(0, null);
	}
	public FirstHideSpinnerAdapter(Object[] array, int showingTextColor) {
		super(array, showingTextColor);
		allLists.add(0, null);
	}
	public FirstHideSpinnerAdapter(Object[] array) {
		super(array);
		allLists.add(0, null);
	}
//	private TextView hideDropDownTv;
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		Object item=getItem(position);
		if(item==null){
			return new TextView(parent.getContext()){
				@Override
				protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
					super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
				}
			};
		}else{
			convertView=null;
			return super.getDropDownView(position, convertView, parent);
		}
		
	}
}
