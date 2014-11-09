package com.fax.faw_vw.menu;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fargment_common.WebViewFragment;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.TopBarContain;

public class QRFragment extends MyFragment {
	boolean canRead = true;
	
	@Override
	public void onResume() {
		super.onResume();
		canRead = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = new MyTopBar(context).setLeftBack().setTitle("二维码扫描")
				.setContentView(R.layout.menu_show_qr);
		
		QRCodeReaderView qrCodeReaderView = (QRCodeReaderView) view.findViewById(R.id.qrCodeReaderView);
		qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
			@Override
			public void onQRCodeRead(String text, PointF[] points) {
				if(!canRead) return;
				canRead = false;
				FragmentContain.start(getActivity(), MyApp.createFragment(WebViewFragment.class, text));
			}
			@Override
			public void cameraNotFound() {
			}
			@Override
			public void QRCodeNotFoundOnCamImage() {
			}
		});
		
		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				View line = view.findViewById(R.id.qrCodeReaderLine);
				int screenHeight = view.getHeight();
				int startY = screenHeight * 330 / 1133 - screenHeight/2;
				int endY = screenHeight * 740 / 1133 - screenHeight/2;
				
				AnimationSet aSet = new AnimationSet(true);
				
				AlphaAnimation aa = new AlphaAnimation(.1f, 1);
				aa.setRepeatMode(Animation.REVERSE);
				aa.setRepeatCount(Animation.INFINITE);
				aa.setDuration(600);
				aSet.addAnimation(aa);
				
				TranslateAnimation ta = new TranslateAnimation(0, 0, startY, endY);
				ta.setDuration(2000);
				ta.setRepeatCount(Animation.INFINITE);
				aSet.addAnimation(ta);
				
				aSet.setRepeatCount(Animation.INFINITE);
				line.startAnimation(aSet);
			}
		});
		
		return view;
	}

}
