package com.fax.faw_vw.fragments_car;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.model.CarModelList;
import com.fax.faw_vw.model.Dealer;
import com.fax.faw_vw.model.Response;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.ResultAsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/** 报价索取 */
public class RequestPriceFragment extends MyFragment
        implements ShowCarsFragment.OnSelectItem, ModelListFragment.OnSelectItem{

    private ShowCarItem car;
    private CarModelList.CarModel carModel;
    private Dealer dealer;
    private EditText name_txt;
    private EditText phone_text;
    private EditText email_text;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_price, container, false);
        //从首页进入的话car为空。从车型中进入的话需要直接选中这个车型
		car = getArguments()==null ? null : getSerializableExtra(ShowCarItem.class);
		dealer = (Dealer) getArguments().getSerializable(Dealer.class.getName());
		name_txt=(EditText) view.findViewById(R.id.name_text);
		phone_text=(EditText) view.findViewById(R.id.phone_text);
		email_text=(EditText) view.findViewById(R.id.email_text);
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
        view.findViewById(R.id.select_shop_button).setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				SearchDealerFragment dealerFragment = new SearchDealerFragment();
    				dealerFragment.setTargetFragment(RequestPriceFragment.this, Request_SelectDealer);
    				addFragment(dealerFragment);
    			}
    		});
            

        view.findViewById(R.id.commit_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 接入接口提交信息（信息未填完Toast提示）
				
				if(TextUtils.isEmpty(name_txt.getText())){
					Toast.makeText(context, "请输入您的姓名", 3000).show();
					return;
				}
				if(TextUtils.isEmpty(phone_text.getText())){
					Toast.makeText(context, "请输入您的手机号码", 3000).show();
					return;
				}
				if(car==null){
					Toast.makeText(context, "请选择车型",3000).show();
					return;
				}
				if(carModel==null){
					Toast.makeText(context, "请选择配置",3000).show();
					return;
				}
				if(TextUtils.isEmpty(email_text.getText())){
					Toast.makeText(context, "请输入您的邮箱", 3000).show();
					return;
				}
				if(dealer==null){
					Toast.makeText(context, "请选择经销商", 3000).show();
					return;
				}
			
				new ResultAsyncTask<Response>(context) {

					@Override
					protected void onPostExecuteSuc(Response result) {
						if(result.getSuccess()==1){
							Toast.makeText(context, "提交成功",3000).show();
							if(context instanceof FragmentActivity){
								if(!((FragmentActivity) context).getSupportFragmentManager().popBackStackImmediate()){
									((Activity) context).finish();
								}
							}else if(context instanceof Activity){
								((Activity) context).finish();
							}
						}else{
							Toast.makeText(context, "提交失败",3000).show();
						}
						
					}

					@Override
					protected Response doInBackground(Void... params) {
						// TODO Auto-generated method stub
						// TODO Auto-generated method stub
						String url="http://faw-vw.allyes.com/index.php?g=api&m=price&a=add";
						ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
						pairs.add(new BasicNameValuePair("adminid",dealer.getId()));
						pairs.add(new BasicNameValuePair("admintitle",dealer.getName()));
						pairs.add(new BasicNameValuePair("truename",name_txt.getText().toString()));
						pairs.add(new BasicNameValuePair("mobile",phone_text.getText().toString()));
						pairs.add(new BasicNameValuePair("email",email_text.getText().toString()));
						pairs.add(new BasicNameValuePair("modelid",car.getId()));
						pairs.add(new BasicNameValuePair("modelname",car.getModel_cn()));
						pairs.add(new BasicNameValuePair("configid",carModel.getId()));
						pairs.add(new BasicNameValuePair("configid",carModel.getModel_name()));
						String json=HttpUtils.reqForGet(url, pairs);
			           try {
						return new Gson().fromJson(json, Response.class);
					} catch (JsonSyntaxException e) {
						// TODO Auto-generated catch block
						
						e.printStackTrace();
						return null;
					}
					}
				}.setProgressDialog().execute();
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
    private void showSelectDealer(){
        if (getView() == null) {
            return;
        }
        Button selectDealerButton = (Button) getView().findViewById(R.id.select_shop_button);
        if (dealer == null) {
        	selectDealerButton.setText("请选择经销商");
        } else {
        	selectDealerButton.setText(dealer.getName());
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
    
    public static final int Request_SelectDealer = 1;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case Request_SelectDealer :
				this.dealer = (Dealer) data.getSerializableExtra(Dealer.class.getName());
				showSelectDealer();
				break;
			}
		}
	}
    
}
