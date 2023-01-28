package com.example.location;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.example.lightweather.MainActivity;

public class LocationListener extends BDAbstractLocationListener {

    private final String TAG = LocationListener.class.getCanonicalName();

    private Location callback;

    //定位接口回调
    public void setCallback(Location callback){
        this.callback = callback;
    }
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if(callback == null){
            Log.e(TAG,"callback is null!");
            return;
        }
        callback.onReceiveLocation(bdLocation);
    }


}
