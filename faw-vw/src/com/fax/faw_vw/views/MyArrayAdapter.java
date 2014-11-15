package com.fax.faw_vw.views;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MyArrayAdapter extends ArrayAdapter {

	public MyArrayAdapter(Context context, Object[] objects) {
		super(context, android.R.layout.simple_spinner_item, objects);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	public MyArrayAdapter(Context context, List objects) {
		super(context, android.R.layout.simple_spinner_item, objects);
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

}
