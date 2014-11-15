package com.fax.faw_vw.findcar;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.findcar.FinancialServiceCoreFragment;
import com.fax.faw_vw.findcar.FinancialServiceProductFragment;
import com.fax.faw_vw.views.MyTopBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**金融服务 */
public class FinancialServiceFragment extends MyFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.financial_service, container, false);
		view.findViewById(R.id.financial_service_core).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//金融引擎
				FragmentContain.start(getActivity(), FinancialServiceCoreFragment.class);
			}
		});
		view.findViewById(R.id.financial_service_product).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//金融产品
				FragmentContain.start(getActivity(), FinancialServiceProductFragment.class);
			}
		});
		view.findViewById(R.id.financial_service_offer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//金融活动
				FragmentContain.start(getActivity(), FinancialServiceOfferFragment.class);
			}
		});
		view.findViewById(R.id.financial_service_detail).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//金融产品详情
				FragmentContain.start(getActivity(), FinancialServiceProductDetailFragment.class);
			}
		});
		view.findViewById(R.id.financial_service_com).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO 购车计算器
				
			}
		});
		view.findViewById(R.id.financial_service_evaluation).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//个人信用度评测
				
			}
		});
		return new MyTopBar(context).setLeftBack().setTitle("金融服务").setContentView(view);
	}
}
