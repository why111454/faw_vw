package com.fax.faw_vw.fragments_main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fax.faw_vw.FragmentContain;
import com.fax.faw_vw.FragmentContainLandscape;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_360.Show360FrameFragment;
import com.fax.faw_vw.fragments_car.CarDetailFragment;
import com.fax.faw_vw.fragments_car.PersonalizedChooseCar;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.model.ShowCarItemRes.CarItemChild;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.view.list.ObjectXAdapter;
import com.fax.utils.view.list.ObjectXListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**产品展示 页卡 */
public class ShowCarsFragment extends MyFragment {

    /** 选择处理器 */
    public OnSelectItem onSelectItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final Context context = getActivity();
		final Class<? extends Fragment> gotoClass = (Class<? extends Fragment>) getSerializableExtra(Class.class);
		MyTopBar view = new MyTopBar(context);
		view.setTitle("车型选择").setContentView(R.layout.main_showcar);
		final boolean isChooseMode = onSelectItem!=null || gotoClass!=null;
		if(isChooseMode){
			view.setLeftBack();
            view.findViewById(R.id.personalized_choose).setVisibility(View.GONE);
            
		}else view.findViewById(R.id.personalized_choose).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startFragment(MyApp.createFragment(PersonalizedChooseCar.class));
			}
		});
		
		ObjectXListView listView = (ObjectXListView) view.findViewById(android.R.id.list);
		listView.setPullRefreshEnable(false);
		listView.setAdapter(new ObjectXAdapter.SingleLocalPageAdapter<ShowCarItem>() {
			@Override
			public View bindView(ShowCarItem t, int position, View view) {
				if(view == null){
					view = View.inflate(context, R.layout.main_showcar_list_item, null);
				}
				((ImageView)view.findViewById(R.id.brand_img)).setImageResource(t.getResId());
				
				LinearLayout nameLayout = (LinearLayout) view.findViewById(R.id.brand_name_layout);
				nameLayout.removeAllViews();
				
				CarItemChild[] itemChildren = t.getDetailRes().getItemChildren();
				if(itemChildren!=null && !isChooseMode){
					//如果有子项，那么显示子项
					for(CarItemChild carItemChild : itemChildren){
						TextView brandCnTv = new TextView(context);
						brandCnTv.setTypeface(Typeface.DEFAULT_BOLD);
						TextView brandEnTv = new TextView(context);
						brandEnTv.setTextColor(getResources().getColor(android.R.color.darker_gray));
						brandCnTv.setText(carItemChild.getName());
						brandEnTv.setText(carItemChild.getNameEn());
						nameLayout.addView(brandCnTv);
						nameLayout.addView(brandEnTv);
					}
					
				}else{
					//没有子项，那么显示自己
					TextView brandCnTv = new TextView(context);
					brandCnTv.setTypeface(Typeface.DEFAULT_BOLD);
					brandCnTv.setTextSize(18);
					TextView brandEnTv = new TextView(context);
					brandEnTv.setTextSize(18);
					brandEnTv.setTextColor(getResources().getColor(android.R.color.darker_gray));
					brandCnTv.setText(t.getModel_cn());
					brandEnTv.setText(t.getModel_en());
					nameLayout.addView(brandCnTv);
					nameLayout.addView(brandEnTv);
				}
				return view;
			}
			@Override
			public List<ShowCarItem> instanceNewList() throws Exception {
				return new ArrayList<ShowCarItem>(Arrays.asList(ShowCarItem.SHOW_CAR_ITEMS));
			}
			@Override
			public void onItemClick(ShowCarItem t, View view, int position, long id) {
				super.onItemClick(t, view, position, id);
				if (gotoClass != null) {//有向前进的Fragment
					if(gotoClass == Show360FrameFragment.class){
						FragmentContainLandscape.start(getActivity(), MyApp.createFragment(gotoClass, t));
					}else FragmentContain.start(getActivity(), MyApp.createFragment(gotoClass, t));
					
				}else if (onSelectItem != null) {
                    backStack();
                    onSelectItem.onSelectCar(t);
                    
                } else {
                    FragmentContain.start(getActivity(), MyApp.createFragment(CarDetailFragment.class, t));
                }
			}
		});

		return view;
	}
    public interface OnSelectItem {
        public void onSelectCar(ShowCarItem item);
    }
}
