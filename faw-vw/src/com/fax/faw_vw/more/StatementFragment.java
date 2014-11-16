package com.fax.faw_vw.more;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import android.os.Bundle;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.views.MyTopBar;

//免责声明
public class StatementFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView textView = new TextView(context);
		textView.setTextSize(16);
		int padding = (int) MyApp.convertToDp(10);
		textView.setPadding(padding, padding, padding, padding);
		try {
			textView.setText(IOUtils.toString(getResources().getAssets().open("statement.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ScrollView scrollView = new ScrollView(context);
		scrollView.addView(textView);
		return new MyTopBar(context).setLeftBack().setTitle("免责声明").setContentView(scrollView);
	}

}
