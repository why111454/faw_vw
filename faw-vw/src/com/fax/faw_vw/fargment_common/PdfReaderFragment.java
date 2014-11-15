package com.fax.faw_vw.fargment_common;

import java.io.File;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.TopBarContain;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PdfReaderFragment extends MyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		PDFView pdfView = new PDFView(context);
		final TopBarContain myTopBar = new MyTopBar(context).setLeftBack()
				.setTitle(R.string.Task_PleaseWait).setContentView(pdfView);
		File file = getSerializableExtra(File.class);
		pdfView.fromFile(file).onPageChange(new OnPageChangeListener() {
			@Override
			public void onPageChanged(int page, int pageCount) {
				myTopBar.setTitle(page+"/"+pageCount);
			}
		}).load();
		return myTopBar;
	}
	
}
