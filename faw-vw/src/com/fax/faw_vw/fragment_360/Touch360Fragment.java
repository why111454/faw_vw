package com.fax.faw_vw.fragment_360;

import java.io.File;
import java.util.ArrayList;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_360.PackedFileLoader.Touch360;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.clickshow.ClickShowButton;
import com.fax.utils.frameAnim.Frame;
import com.fax.utils.frameAnim.FrameTouchControler;
import com.fax.utils.frameAnim.FrameTouchControler.TouchControlerListener;

public class Touch360Fragment extends MyFragment {
	TouchControlerListener listener = new TouchControlerListener() {
		@Override
		public void onFrameChange(Frame frame, int index) {
			nowIndex = index;
		}
	};
	int nowIndex;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ShowCarItem carItem = (ShowCarItem) getArguments().getSerializable(ShowCarItem.class.getName());
		PackedFileLoader fileLoader = PackedFileLoader.getInstance(context, carItem);
		ArrayList<Touch360> touch360s = fileLoader.getTouch360List();
		final View view = inflater.inflate(R.layout.show360_touch_frames, container, false);
		final ImageView imageView = (ImageView) view.findViewById(android.R.id.background);

		LinearLayout linear = (LinearLayout) view.findViewById(android.R.id.summary);
		for(int i=0,size=touch360s.size(); i<size; i++){
			final Touch360 touch360 = touch360s.get(i);
			ImageButton tv = new ImageButton(context);
			tv.setBackgroundResource(R.drawable.common_btn_in_white);
			tv.setScaleType(ScaleType.CENTER_INSIDE);
			Drawable drawable = touch360.btnFrame.decodeDrawable(context);
			tv.setImageDrawable(drawable);
			tv.setAdjustViewBounds(true);
			int padding = (int) MyApp.convertToDp(6);
			tv.setPadding(padding, padding, padding, padding);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FrameTouchControler.controlView(imageView, touch360.frames, nowIndex, false, listener);
				}
			});
			linear.addView(tv, new LinearLayout.LayoutParams(-2, -1));
		}
		view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backStack();
			}
		});
		FrameTouchControler.controlView(imageView, touch360s.get(0).frames, listener);
		return view;
	}
	
}
