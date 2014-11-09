package com.fax.faw_vw.views;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MySpinnerAdapter<T> extends BaseAdapter implements SpinnerAdapter {
	ArrayList<T> allLists;
	private static final int DefaultShowingTextColor=Color.BLACK;
	private int showingTextColor;
	public MySpinnerAdapter(ArrayList<T> allLists) {
		this(allLists, DefaultShowingTextColor);
	}
	public MySpinnerAdapter(ArrayList<T> allLists,int showingTextColor) {
		this.allLists=new ArrayList<T>();
		for(T o: allLists){
			if(o!=null) this.allLists.add(o);
//			else this.allLists.add(null);
		}
		this.showingTextColor = showingTextColor;
	}
	public MySpinnerAdapter(T[] allLists) {
		this(allLists, DefaultShowingTextColor);
	}
	public MySpinnerAdapter(T[] allLists,int showingTextColor) {
		this.allLists=new ArrayList<T>();
		for(T o: allLists){
			if(o!=null) this.allLists.add(o);
//			else this.allLists.add("");
		}
		this.showingTextColor = showingTextColor;
	}

	@Override
	public int getCount() {
		return allLists.size();
	}

	@Override
	public Object getItem(int position) {
		return allLists.get(position);
	}
	public int getItemIndex(String object){
		return allLists.indexOf(object);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public TextView getView(int position, View convertView, final ViewGroup parent) {
		TextView text = (TextView) convertView;
		if(text==null) text = new TextView(parent.getContext());
		text.setTextColor(showingTextColor);
		text.setGravity(Gravity.CENTER);
		T s=allLists.get(position);
		text.setText(s==null?null:s.toString());
		return text;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView text = new TextView(parent.getContext());
		int padding=(int) (10*parent.getContext().getResources().getDisplayMetrics().density);
		text.setPadding(padding, padding, padding, padding);
//		text.setTextColor(Color.WHITE);
		T s=allLists.get(position);
		text.setText(s==null?null:s.toString());
		
		return text;
//		View view=View.inflate(parent.getContext(), R.layout.spinner_item, null);
//		TextView text = (TextView) view.findViewById(android.R.id.text1);
//		text.setText(allLists.get(position).toString());
//		return view;
	}

}
