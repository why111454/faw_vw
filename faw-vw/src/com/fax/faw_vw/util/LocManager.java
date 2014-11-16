package com.fax.faw_vw.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class LocManager {
    public static AMapLocation location;

    public static String getLocationXy(){
        if(location==null) return null;
        if(location.getLatitude()==0 && location.getLongitude()==0) return null;
        return location.getLatitude() +","+ location.getLongitude();
    }
    public static String getLocationCityName(){
        if(location==null) return null;
        return location.getCity();
    }

    public static void reqLocOnce(Context context) {
        LocationManagerProxy mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 60 * 1000, -1, locationListener);

        mLocationManagerProxy.setGpsEnable(false);
    }
    
    public static void reqLoc(Context context,final AMapLocationListener locationListener) {
        LocationManagerProxy mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        mLocationManagerProxy.requestLocationUpdates(
                LocationProviderProxy.AMapNetwork, 60 * 1000, -1, new AMapLocationListener() {
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
						if(locationListener!=null) locationListener.onStatusChanged(provider, status, extras);
					}
					@Override
					public void onProviderEnabled(String provider) {
						if(locationListener!=null) locationListener.onProviderEnabled(provider);
					}
					@Override
					public void onProviderDisabled(String provider) {
						if(locationListener!=null) locationListener.onProviderDisabled(provider);
					}
					@Override
					public void onLocationChanged(Location location) {
						if(locationListener!=null) locationListener.onLocationChanged(location);
					}
					@Override
					public void onLocationChanged(AMapLocation aMapLocation) {
			            LocManager.location = aMapLocation;
						if(locationListener!=null) locationListener.onLocationChanged(aMapLocation);
					}
				});

        mLocationManagerProxy.setGpsEnable(false);
    }
    private static AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LocManager.location = aMapLocation;
        }
        @Override
        public void onLocationChanged(Location location) {
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

}
