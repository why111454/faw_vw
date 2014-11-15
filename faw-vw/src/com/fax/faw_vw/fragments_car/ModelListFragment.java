package com.fax.faw_vw.fragments_car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.model.CarModelList;
import com.fax.faw_vw.model.CarModelList.CarModel;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.bitmap.BitmapManager;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;
import com.google.gson.Gson;

import java.util.List;

public class ModelListFragment extends MyFragment {
    /** 选择处理器 */
    public OnSelectItem onSelectItem;

	public static ModelListFragment newInstance(ShowCarItem carItem, Class<? extends Fragment> gotoFragment){
		ModelListFragment fragment = new ModelListFragment();
		fragment.setArguments(new Intent().putExtra(ShowCarItem.class.getName(), carItem).putExtra(Class.class.getName(), gotoFragment).getExtras());
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ObjectXListView listView = new ObjectXListView(context);
		final ShowCarItem carItem = (ShowCarItem) getArguments().getSerializable(ShowCarItem.class.getName());
		listView.setAdapter(new ObjectXAdapter.SinglePageAdapter<CarModel>() {
			@Override
			public String getUrl() {
				return MyApp.getApiUrl("task=get_carmodel_by_brand&brand_id="+carItem.getId());
			}
			@Override
			public View bindView(CarModel t, int position, View convertView) {
				if(convertView== null) convertView = View.inflate(context, R.layout.car_model_list_item, null);
				((TextView)convertView.findViewById(R.id.model_name)).setText(t.getModel_name());
				BitmapManager.bindView(convertView.findViewById(R.id.model_img), t.getComparepicUrl());
				return convertView;
			}
			@Override
			public List<CarModel> instanceNewList(String json) throws Exception {
				return new Gson().fromJson(json, CarModelList.class).getMsg();
			}
			@Override
			public void onItemClick(CarModel t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);

                if (onSelectItem != null) {
                    getFragmentManager().popBackStack();
                    onSelectItem.onSelectCarModel(t);
                    return;
                }

				Bundle args = getArguments();
				args.putSerializable(CarModel.class.getName(), t);
				
				Class<? extends Fragment> c = (Class<? extends Fragment>) getArguments().getSerializable(Class.class.getName());
				if (c != null) {//有向前进的Fragment
					try {
						Fragment fragment = c.newInstance();
						args.remove(Class.class.getName());
						fragment.setArguments(args);
						addFragment(fragment);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(getTargetFragment()!=null){//如果有TargetFragment，就把数据返回给这个Fragment
					getFragmentManager().popBackStack();
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtras(args));
					
				}else if(!getFragmentManager().popBackStackImmediate()){//不能返回就结束Activity
					getActivity().setResult(Activity.RESULT_OK, new Intent().putExtras(args));
					getActivity().finish();
				}
			}
		});
		return new MyTopBar(getActivity()).setLeftBack()
				.setTitle("选择模型").setContentView(listView);
	}

    public interface OnSelectItem {
        public void onSelectCarModel(CarModel item);
    }
}
