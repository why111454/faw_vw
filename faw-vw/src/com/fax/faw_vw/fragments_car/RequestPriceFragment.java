package com.fax.faw_vw.fragments_car;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.model.CarModelList;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;

/** 报价索取 */
public class RequestPriceFragment extends MyFragment
        implements ShowCarsFragment.OnSelectItem, ModelListFragment.OnSelectItem{

    private ShowCarItem car;
    private CarModelList.CarModel carModel;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_price, container, false);
        //从首页进入的话car为空。从车型中进入的话需要直接选中这个车型
		car = getArguments()==null ? null : getSerializableExtra(ShowCarItem.class);

        view.findViewById(R.id.select_car_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCarsFragment showCarsFragment = new ShowCarsFragment();
                showCarsFragment.onSelectItem = RequestPriceFragment.this;
                addFragment(showCarsFragment);
            }
        });

        view.findViewById(R.id.select_car_model_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (car == null) {
                    Toast.makeText(context, "请先选择车辆", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModelListFragment modelListFragment = ModelListFragment.newInstance(car, null);
                modelListFragment.onSelectItem = RequestPriceFragment.this;
                addFragment(modelListFragment);
            }
        });

        view.findViewById(R.id.commit_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 接入接口提交信息（信息未填完Toast提示）
				
			}
		});
        return new MyTopBar(getActivity()).setLeftBack()
                .setTitle("报价索取").setContentView(view);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	super.onViewCreated(view, savedInstanceState);
        showSelectedCar();
    }

    private void showSelectedCar() {
        if (getView() == null || car == null) {
            return;
        }
        Button selectCarButton = (Button) getView().findViewById(R.id.select_car_button);
        selectCarButton.setText(car.getModel_cn());
    }

    private void showSelectedCarModel() {
        if (getView() == null) {
            return;
        }
        Button selectCarButton = (Button) getView().findViewById(R.id.select_car_model_button);
        if (carModel == null) {
            selectCarButton.setText("选择配置");
        } else {
            selectCarButton.setText(carModel.getModel_name());
        }
    }


    @Override
    public void onSelectCar(ShowCarItem car) {
        if (this.car != null && !this.car.getId().equals(car.getId())) {
            this.carModel = null;
            showSelectedCarModel();
        }
        this.car = car;
        showSelectedCar();
    }

    @Override
    public void onSelectCarModel(CarModelList.CarModel carModel) {
        this.carModel = carModel;
        showSelectedCarModel();
    }
}
