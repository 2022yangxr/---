package com.example.lightweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;

import com.baidu.location.LocationClient;

public class GpsRoot extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //申请定位权限
        LocationClient.setAgreePrivacy(true);
    }
}