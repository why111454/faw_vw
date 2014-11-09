package com.fax.utils.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**可以多行布局的RadioGroup，会覆盖RadioButton的OnCheckedChangeListener */
public class MultiLineRadioGroup extends RadioGroup {

	public MultiLineRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MultiLineRadioGroup(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
			public void onChildViewRemoved(View parent, View child) {
				if (parent == MultiLineRadioGroup.this && child instanceof ViewGroup) {
					for(RadioButton radioButton:getRadioButtonFromGroup((ViewGroup) child)){
						radioButton.setOnCheckedChangeListener(null);
					}
	            }
			}
			@SuppressLint("NewApi")
			@Override
			public void onChildViewAdded(View parent, View child) {
				if (parent == MultiLineRadioGroup.this && child instanceof ViewGroup) {
					for(final RadioButton radioButton : getRadioButtonFromGroup((ViewGroup) child)){
		                int id = radioButton.getId();
		                // generates an id if it's missing
		                if (id == View.NO_ID) {
		                	if(VERSION.SDK_INT>=17) id = View.generateViewId();
		                	else id = radioButton.hashCode();
		                	radioButton.setId(id);
		                }
		                if(radioButton.isChecked()){
		                	check(id);
		                }
		                
						radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
								if(isChecked){
									radioButton.setOnCheckedChangeListener(null);
									MultiLineRadioGroup.this.check(buttonView.getId());
									radioButton.setOnCheckedChangeListener(this);
								}
							}
						});
						
					}
	            }
			}
		});
	}
	
	private boolean checking=false;
	@Override
	public void check(int id) {
		if(checking) return;
		checking=true;
		super.check(id);
		checking=false;
	}

	private ArrayList<RadioButton> getRadioButtonFromGroup(ViewGroup group){
		if(group==null) return new ArrayList<RadioButton>();
		ArrayList<RadioButton> list=new ArrayList<RadioButton>();
		getRadioButtonFromGroup(group, list);
		return list;
	}
	private void getRadioButtonFromGroup(ViewGroup group, ArrayList<RadioButton> list){
		for(int i=0,count=group.getChildCount();i<count;i++){
			View child = group.getChildAt(i);
			if(child instanceof RadioButton){
				list.add((RadioButton) child);
			}
		}
	}
	
}
