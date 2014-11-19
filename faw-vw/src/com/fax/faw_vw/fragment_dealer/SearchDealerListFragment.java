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
import com.fax.faw_vw.views.FirstHideSpinnerAdapter;
import com.fax.faw_vw.views.MySpinnerAdapter;
import com.fax.faw_vw.views.MyTopBar;
import com.fax.utils.task.HttpAsyncTask;
import com.fax.utils.task.ResultAsyncTask;
import com.fax.utils.view.TopBarContain;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**经销商查询 */
public class SearchDealerListFragment extends MyFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.search_dealer, container, false);
		MyTopBar topBar = (MyTopBar) new MyTopBar(context);
		
		return topBar.setLeftBack()
				.setTitle("经销商").setContentView(view);
	}
	
}



