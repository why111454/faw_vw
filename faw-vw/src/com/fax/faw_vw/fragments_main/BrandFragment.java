package com.fax.faw_vw.fragments_main;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.brand.BrandInnovationFragment;
import com.fax.faw_vw.brand.BrandShowFragment;
import com.fax.faw_vw.views.MyTopBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**了解品牌 页卡 */
public class BrandFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_brand, container, false);
		view.findViewById(R.id.main_brand_show).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//品牌展示
				FragmentContain.start(getActivity(), BrandShowFragment.class);
			}
		});
		view.findViewById(R.id.main_brand_innovation).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//科技创新
				FragmentContain.start(getActivity(), BrandInnovationFragment.class);
			}
		});
		return new MyTopBar(context).setTitle("了解品牌").setContentView(view);
	}
}
