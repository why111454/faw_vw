package com.fax.faw_vw.fragment_dealer;

import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.fax.faw_vw.MyApp;
import com.fax.faw_vw.MyFragment;
import com.fax.faw_vw.R;
import com.fax.faw_vw.fragments_car.BookDriveFragment;
import com.fax.faw_vw.fragments_car.OnlineOrderCarFragment;
import com.fax.faw_vw.fragments_car.RequestPriceFragment;
import com.fax.faw_vw.model.CityInfo;
import com.fax.faw_vw.model.Dealer;
import com.fax.faw_vw.model.ProvinceList;
import com.fax.faw_vw.model.ProvinceList.Province;
import com.fax.faw_vw.more.QueryIllegalFragment;
import com.fax.faw_vw.views.FirstHideSpinnerAdapter;
import com.fax.faw_vw.views.MySpinnerAdapter;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.task.HttpAsyncTask;
import com.fax.utils.task.ResultAsyncTask;
import com.fax.utils.view.TopBarContain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**经销商查询 */
public class SearchDealerFragment extends MyFragment {

	private FirstHideSpinnerAdapter emptyAdapter = new FirstHideSpinnerAdapter(new String[]{null}){
		@Override
		public TextView getView(int position, View convertView, ViewGroup parent) {
			TextView tv = super.getView(position, convertView, parent);
			tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_arrow_down, 0);
			return tv;
		}
	};
	ProvinceList provinceList;
	Spinner provinceSpinner;
	Spinner citySpinner;
	private void initProvinceCitySpinner(View view) {
		try {
			String json = IOUtils.toString(getActivity().getAssets().open("cityConfig.json"));
			provinceList = new Gson().fromJson(json, ProvinceList.class);
			provinceSpinner = (Spinner) view.findViewById(R.id.search_dealer_province_spinner);
			citySpinner = (Spinner) view.findViewById(R.id.search_dealer_city_spinner);
			//初始化citySpinner的状态
			citySpinner.setAdapter(emptyAdapter);
			citySpinner.setClickable(false);
			
			provinceSpinner.setAdapter(new FirstHideSpinnerAdapter(provinceList.getData()){
				@Override
				public TextView getView(int position, View convertView, ViewGroup parent) {
					TextView tv = super.getView(position, convertView, parent);
					tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_arrow_down, 0);
					return tv;
				}
			});
			provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					final Province province = (Province)provinceSpinner.getItemAtPosition(position);
					if(province == null) return;
					//开始异步加载城市列表
					citySpinner.setOnItemSelectedListener(null);
					citySpinner.setAdapter(emptyAdapter);
					new HttpAsyncTask<ArrayList<CityInfo>>(context, MyApp.getCityListUrl(province)) {
						@Override
						protected ArrayList<CityInfo> instanceObject(String json) throws Exception {
							return new Gson().fromJson(json, new TypeToken<ArrayList<CityInfo>>(){}.getType());
						}
						@Override
						protected void onPostExecuteSuc(ArrayList<CityInfo> result) {
							citySpinner.setClickable(true);

							citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
								@Override
								public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
									chooseCityInfo((CityInfo) citySpinner.getItemAtPosition(position));
								}
								@Override
								public void onNothingSelected(AdapterView<?> parent) {
								}
							});
							
							citySpinner.setAdapter(new MySpinnerAdapter<CityInfo>(result){
								@Override
								public TextView getView(int position, View convertView, ViewGroup parent) {
									TextView tv = super.getView(position, convertView, parent);
									tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_arrow_down, 0);
									return tv;
								}
							});

						}
					}.setProgressDialog().execute();
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		} catch (Exception e) {
		}
	}

	private void chooseCityInfo(CityInfo cityInfo){
		//清除所有加入的Marker
        for (Marker marker : aMap.getMapScreenMarkers()) {
            if (marker.getObject() instanceof Dealer) marker.remove();
        }
        
        //异步获取所有经销商
        new HttpAsyncTask<ArrayList<Dealer>>(context, MyApp.getDealerListUrl(cityInfo)) {
			@Override
			protected ArrayList<Dealer> instanceObject(String json) throws Exception {
				return new Gson().fromJson(json, new TypeToken<ArrayList<Dealer>>(){}.getType());
			}
			@Override
			protected void onPostExecuteSuc(ArrayList<Dealer> dealers) {
				
		        //地图显示到选中的城市
		        LatLngBounds.Builder build = new LatLngBounds.Builder();
				for(Dealer dealer : dealers){
					Marker marker = aMap.addMarker(new MarkerOptions().position(dealer.getLatLng())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_ic_marker))
//							.anchor(1.0f, 1)//FIXME 高德地图2D版的bug，论坛中说会在下个版本修复，现在先用3D的吧
							.title(dealer.getFullname()).snippet(dealer.getSnippet()));
					build.include(marker.getPosition());
					marker.setObject(dealer);
					
				}
                // 移动地图，所有marker自适应显示。LatLngBounds与地图边缘10像素的填充区域
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build.build(), 10));
			}
		}.setProgressDialog().execute();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_dealer, container, false);
		initProvinceCitySpinner(view);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context);
		
		topBar.setRightBtn("", R.drawable.search_dealer_ic_list, new OnClickListener(){
				@Override
				public void onClick(View v) {
					SearchDealerListFragment searchDealerListFragment=new  SearchDealerListFragment();
					addFragment(searchDealerListFragment);
				}});
		
		mapView = new MapView(context);
		((FrameLayout)view.findViewById(R.id.contain_map)).addView(mapView, -1, -1);
		mapView.onCreate(savedInstanceState);
		setUpMap();
		
		return topBar.setLeftBack()
				.setTitle("经销商查询").setContentView(view);
	}
	
	
	
	DrivingRouteOverlay drivingRouteOverlay;
	Dealer showingDealer;
	//画出地点的详情窗口
	public void render(final Marker marker, final View view) {
		((TextView) view.findViewById(R.id.title)).setText(marker.getTitle());
		((TextView) view.findViewById(R.id.snippet)).setText(marker.getSnippet());

		final Dealer dealer = (Dealer) marker.getObject();
		if(dealer==null){//说明不是经销商的marker
			view.findViewById(R.id.map_info_contents_layout).setVisibility(View.GONE);
			return;
		}
		view.findViewById(R.id.map_info_contents_layout).setVisibility(View.VISIBLE);
		if(showingDealer == dealer) return;//同一个经销商
		showingDealer = dealer;
		
		view.findViewById(R.id.map_info_contents_navi).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//导航
				Location l = aMap.getMyLocation();
				FromAndTo fromAndTo=new FromAndTo(new LatLonPoint(l.getLatitude(), l.getLongitude()), 
						new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));
				final DriveRouteQuery query = new DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null, null, "");  
				final RouteSearch routeSearch = new RouteSearch(context);
				new ResultAsyncTask<DriveRouteResult>(context) {
					@Override
					protected void onPostExecuteSuc(DriveRouteResult result) {
						if(result != null && result.getPaths() != null && result.getPaths().size() > 0){
	    	                DrivePath drivePath = result.getPaths().get(0);
//	    	                aMap.clear();//清理之前的图标
	    	                marker.hideInfoWindow();
	    	                if(drivingRouteOverlay!=null) drivingRouteOverlay.removeFromMap();
							drivingRouteOverlay = new DrivingRouteOverlay(context, aMap, drivePath, result
									.getStartPos(), result.getTargetPos());
	    	                drivingRouteOverlay.removeFromMap();
	    	                drivingRouteOverlay.addToMap();
	    	                drivingRouteOverlay.zoomToSpan();           
	    	            }else{
	    	            	Toast.makeText(context, "没有找到合适的路线", Toast.LENGTH_SHORT).show();
	    	            }
					}
					@Override
					protected DriveRouteResult doInBackground(Void... params) {
						try {
							return routeSearch.calculateDriveRoute(query);
						} catch (AMapException e) {
							e.printStackTrace();
						}
						return null;
					}
				}.setProgressDialog().execute();
			}
		});

		TextView chooseBtn = (TextView) view.findViewById(R.id.map_info_contents_order_drive);
		if(getTargetFragment() instanceof RequestPriceFragment){
			chooseBtn.setText("报价索取");
		}else if(getTargetFragment() instanceof OnlineOrderCarFragment){
			chooseBtn.setText("在线订车");
		}else if(getTargetFragment() instanceof BookDriveFragment){
			chooseBtn.setText("预约试驾");
		}
		chooseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//预约试驾
				if(getTargetFragment() instanceof BookDriveFragment
						|| getTargetFragment() instanceof RequestPriceFragment
						|| getTargetFragment() instanceof OnlineOrderCarFragment){
					backStack();
					getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, MyApp.createIntent(dealer));
				}
			}
		});
		view.findViewById(R.id.map_info_contents_tel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//电话联系
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dealer.getSelltel()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		Animation a = AnimationUtils.loadAnimation(context, R.anim.alpha_small_to_normal);
		a.setInterpolator(new BounceInterpolator());
		a.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {//解决动画完后定死的bug
				view.postDelayed(new Runnable() {
					@Override
					public void run() {
						marker.hideInfoWindow();
						marker.showInfoWindow();
					}
				}, 100);
			}
		});
		view.setAnimation(a);
	}

	boolean isChangedCity;//标识是否首次进入App自动定位到城市并选择
	AMapLocation myLocation;
	private void tryGotoMyCity(){
    	if(isChangedCity || provinceList==null || myLocation==null) return;
    	isChangedCity = true;

    	LatLng locLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
    	
    	ProvinceList.Province wantProvince = null;
    	float minDistance = Float.MAX_VALUE;
    	for(ProvinceList.Province province : provinceList.getData()){
    		float distance = AMapUtils.calculateLineDistance(province.getLatLng(), locLatLng);
    		if(wantProvince == null || distance < minDistance){
    			minDistance = distance;
    			wantProvince = province;
    		}
    	}
    	if(wantProvince!=null){
    		for(int i=0,count= provinceSpinner.getAdapter().getCount();i<count;i++){
    			if(wantProvince.equals(provinceSpinner.getItemAtPosition(i)))
    	    		provinceSpinner.setSelection(i);
    		}
    	}
	}
    /** 
     * 定位成功后回调函数 
     */  
    public void onLocChanged(AMapLocation aLocation) {
    	myLocation = aLocation;
    	tryGotoMyCity();
    }
	

	//---------------------以下方法为地图初始化部分。ps：这么长，蛋疼
    /** 
     * 此方法需存在 
     */  
    @Override
	public void onResume() {  
        super.onResume();  
        mapView.onResume();  
    }  
    /** 
     * 此方法需存在 
     */  
    @Override
	public void onPause() {  
        super.onPause();  
        mapView.onPause();  
        if(locationSource!=null) locationSource.deactivate();
    }  
  
    /** 
     * 此方法需存在 
     */  
    @Override
	public void onDestroy() {  
        super.onDestroy();  
        mapView.onDestroy();  
    }  
    
	private AMap aMap;  
    private MapView mapView;  
    private OnLocationChangedListener mListener;  
    LocationSource locationSource;
    private Marker lastShowInfoMarker;
	public static final LatLng SHANGHAI = new LatLng(31.238068, 121.501654);// 上海市经纬度
	CameraPosition LUJIAZUI = new CameraPosition.Builder().target(SHANGHAI).zoom(11).bearing(0).tilt(30).build();
    private void setUpMap() {  
        if (aMap == null) {
            aMap = mapView.getMap();  
        }
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(LUJIAZUI));
        
        // 自定义系统定位小蓝点  
        MyLocationStyle myLocationStyle = new MyLocationStyle();  
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_marker));  
        myLocationStyle.strokeColor(Color.BLACK);  
        myLocationStyle.strokeWidth(3);
        aMap.setMyLocationStyle(myLocationStyle);  

        //mark的View的显示
//        final View mWindow = View.inflate(context, R.layout.map_info_window, null);
        final View mContents = View.inflate(context, R.layout.map_info_contents, null);
        aMap.setInfoWindowAdapter(new InfoWindowAdapter() {
        	@Override
        	public View getInfoContents(Marker marker) {
        		render(marker, mContents);
        		return mContents;
        	}
        	@Override
        	public View getInfoWindow(Marker marker) {
        		return null;
        	}
		});
//        aMap.setOnMarkerDragListener(this);
        aMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				if (marker.isInfoWindowShown()) {
					marker.hideInfoWindow();
				} else {
					aMap.animateCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()), new CancelableCallback(){
						@Override
						public void onCancel() {
						}
						@Override
						public void onFinish() {
							marker.showInfoWindow();
							lastShowInfoMarker = marker;
						}
					});
				}
				return true;
			}
		});
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latlng) {
				if(lastShowInfoMarker!=null){
					lastShowInfoMarker.hideInfoWindow();
				}
			}
		});
        
        final AMapLocationListener locationListener = new AMapLocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
			@Override
			public void onProviderEnabled(String provider) {
			}
			@Override
			public void onProviderDisabled(String provider) {
			}
			@Override
			public void onLocationChanged(Location location) {
			}
		    /** 
		     * 定位成功后回调函数 
		     */  
		    @Override  
		    public void onLocationChanged(AMapLocation aLocation) {  
		        if (mListener != null) {
		        	CameraPosition p=aMap.getCameraPosition();
		            // 将定位信息显示在地图上  
		            mListener.onLocationChanged(aLocation);
		            
		            onLocChanged(aLocation);
		        }
		    }
		};
        // 构造 LocationManagerProxy 对象  
		final LocationManagerProxy mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());  
        //设置定位资源。如果不设置此定位资源则定位按钮不可点击。  
		locationSource = (new LocationSource(){
			@Override
			public void activate(OnLocationChangedListener listener) {
		        if (mListener == null) {
	    			 //1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true 
//	            	mAMapLocationManager.setGpsEnable(false);
	    			// Location SDK定位采用GPS和网络混合定位方式，时间最短是5000毫秒，否则无效  
	    			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, locationListener);
		        }
		        mListener = listener;  
			}
		    /** 
		     * 停止定位 
		     */  
		    @Override  
		    public void deactivate() {  
		        mListener = null;  
		        if (mAMapLocationManager != null) {  
		            mAMapLocationManager.removeUpdates(locationListener);  
		            mAMapLocationManager.destory();  
		        }  
		    }
        });
		aMap.setLocationSource(locationSource);
		
        //设置默认定位按钮是否显示  
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false  
        aMap.setMyLocationEnabled(true);   
    } 
}



