package com.fax.faw_vw.fragments_car;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragment_dealer.SearchDealerFragment;
import com.fax.faw_vw.fragments_main.ShowCarsFragment;
import com.fax.faw_vw.model.CarModelList;
import com.fax.faw_vw.model.Dealer;
import com.fax.faw_vw.model.Respon;
import com.fax.faw_vw.model.Response;
import com.fax.faw_vw.model.ShowCarItem;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.http.HttpUtils;
import com.fax.utils.task.ResultAsyncTask;
import com.google.gson.Gson;

/** 预约试驾 */
public class BookDriveFragment extends MyFragment
        implements ShowCarsFragment.OnSelectItem, ModelListFragment.OnSelectItem{

    private ShowCarItem car;
    private CarModelList.CarModel carModel;
    private Dealer dealer;
    private String bookdate;
    private String buydate;
    private EditText name_text,phone_text;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.book_drive, container, false);
        //从首页进入的话car为空。从车型中进入的话需要直接选中这个车型
		car = (ShowCarItem) getArguments().getSerializable(ShowCarItem.class.getName());
		dealer = (Dealer) getArguments().getSerializable(Dealer.class.getName());

        view.findViewById(R.id.select_car_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCarsFragment showCarsFragment = new ShowCarsFragment();
                showCarsFragment.onSelectItem = BookDriveFragment.this;
                addFragment(showCarsFragment);
            }
        });
        
        view.findViewById(R.id.select_car_model_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (car == null) {
                    Toast.makeText(context, "请先选择车型", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModelListFragment modelListFragment = ModelListFragment.newInstance(car, null);
                modelListFragment.onSelectItem = BookDriveFragment.this;
                addFragment(modelListFragment);
            }
        });
        view.findViewById(R.id.select_dealer_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchDealerFragment dealerFragment = new SearchDealerFragment();
				dealerFragment.setTargetFragment(BookDriveFragment.this, Request_SelectDealer);
				addFragment(dealerFragment);
			}
		});
        RadioGroup radioGroup1=(RadioGroup) view.findViewById(R.id.radio_group1);
        bookdate=(String) ((RadioButton) radioGroup1.findViewById(radioGroup1.getCheckedRadioButtonId())).getText();
		radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		   @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId) {  
                //点击事件获取的选择对象  
			      RadioButton  checkRadioButton = (RadioButton) view.findViewById(checkedId); 
			      bookdate=(String) checkRadioButton.getText();
            }  
        });
		RadioGroup radioGroup2=(RadioGroup) view.findViewById(R.id.radio_group2);
		buydate=(String) ((RadioButton) radioGroup2.findViewById(radioGroup2.getCheckedRadioButtonId())).getText();
		radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override  
			public void onCheckedChanged(RadioGroup group, int checkedId) {  
				//点击事件获取的选择对象  
				RadioButton  checkRadioButton = (RadioButton) view.findViewById(checkedId); 
				buydate=(String) checkRadioButton.getText();
			}  
		});
        
        view.findViewById(R.id.commit_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 接入接口提交信息（信息未填完Toast提示）
				name_text=	(EditText) view.findViewById(R.id.name_text);
				phone_text=(EditText) view.findViewById(R.id.phone_text);
				if(TextUtils.isEmpty(name_text.getText().toString())){
					Toast.makeText(context, "请输入您的姓名", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(phone_text.getText().toString())){
					Toast.makeText(context, "请输入您的手机号码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (car == null) {
                    Toast.makeText(context, "请先选择车辆", Toast.LENGTH_SHORT).show();
                    return;
                }
				if(carModel==null){
					Toast.makeText(context, "请先选择配置", Toast.LENGTH_SHORT).show();
					return;
				}
				if(dealer==null){
					Toast.makeText(context, "请先选择经销商", Toast.LENGTH_SHORT).show();
					return;
				}
				new ResultAsyncTask<Response>(context) {
					@Override
					protected Response doInBackground(Void... params) {
						final ArrayList<NameValuePair> pairs=new ArrayList<NameValuePair>();
						pairs.add(new BasicNameValuePair("adminid", dealer.getId()));
						pairs.add(new BasicNameValuePair("admintitle", dealer.getName()));
						pairs.add(new BasicNameValuePair("truename", name_text.getText().toString()));
						pairs.add(new BasicNameValuePair("mobile", phone_text.getText().toString()));
						pairs.add(new BasicNameValuePair("modelid", car.getId()));
						pairs.add(new BasicNameValuePair("modelname", car.getModel_cn()));
						pairs.add(new BasicNameValuePair("configid", carModel.getId()));
						pairs.add(new BasicNameValuePair("configname", carModel.getModel_name()));
						pairs.add(new BasicNameValuePair("bookdate",bookdate));
						pairs.add(new BasicNameValuePair("buydate", buydate));
						String json=HttpUtils.reqForGet("http://faw-vw.allyes.com/index.php?g=api&m=book&a=add",pairs);
						try {
							return new Gson().fromJson(json, Response.class);
						} catch (Exception e) {
						}
						return null;
					}
					@Override
					protected void onPostExecute(Response result) {
					 if(result.getSuccess()==1){//登陆成功
						 Toast.makeText(context, "信息提交成功", Toast.LENGTH_SHORT).show();
							if(!((FragmentActivity) context).getSupportFragmentManager().popBackStackImmediate()){
								((Activity) context).finish();
							}
						}else Toast.makeText(context, "信息提交失败", Toast.LENGTH_SHORT).show();
						super.onPostExecute(result);
					}
					@Override
					protected void onPostExecuteSuc(Response result) {
						// TODO Auto-generated method stub
					}
				}.setProgressDialog().execute();
			}
		});

        return new MyTopBar(getActivity()).setLeftBack()
                .setTitle("预约试驾").setContentView(view);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        showSelectedCar();
        showSelectDealer();
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
        Button selectDealerButton = (Button) getView().findViewById(R.id.select_dealer_button);
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
