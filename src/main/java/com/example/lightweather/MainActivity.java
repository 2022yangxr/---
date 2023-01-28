package com.example.lightweather;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.lightweather.databinding.ActivityMainBinding;
import com.example.location.Location;
import com.example.location.LocationListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Location {

    public ActivityMainBinding binding;
    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;

    private void registerIntent() {
        //请求权限意图
        requestPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean fineLocation = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
            boolean writeStorage = Boolean.TRUE.equals(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            if (fineLocation && writeStorage) {
                //权限已经获取到，开始定位
                startGPS();
            }
        });
    }

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //开始权限请求
            requestPermissionIntent.launch(permissions);
            return;
        }
        //开始定位
        startGPS();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initLocation();
        requestPermission();

    }

    public LocationClient mLocationClient= null;
    private  final LocationListener locationListener = new LocationListener();

    private  void  initLocation(){
        try {
            mLocationClient = new LocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
     if(mLocationClient != null){
         locationListener.setCallback(this);
         mLocationClient.registerLocationListener(locationListener);
         LocationClientOption option = new LocationClientOption();
         option.setIsNeedAddress(true);
         option.setNeedNewVersionRgc(true);
         mLocationClient.setLocOption(option);
     }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();    //获取纬度信息
        double longitude = bdLocation.getLongitude();    //获取经度信息
        float radius = bdLocation.getRadius();    //获取定位精度
        String coorType = bdLocation.getCoorType();//获取经纬度坐标类型
        int errorCode = bdLocation.getLocType();//161  表示网络定位结果
        String address = bdLocation.getAddrStr();    //获取详细地址信息
        String country = bdLocation.getCountry();    //获取国家
        String province = bdLocation.getProvince();    //获取省份
        String city = bdLocation.getCity();    //获取城市
        String district = bdLocation.getDistrict();    //获取区县
        String street = bdLocation.getStreet();    //获取街道信息
        String locationDescribe = bdLocation.getLocationDescribe();    //获取位置描述信息
        binding.tvGetAddress.setText(address);//设置文本显示

        searchCity(district);
    }

    public void startGPS(){
        if(mLocationClient != null){
            mLocationClient.start();
        }
    }

    private  void  searchCity(String district) {
        //Okhttp
        OkHttpClient client =new OkHttpClient();
        Request request = new Request.Builder().url("https://geoapi.qweather.com/v2/city/lookup?key=c6ff6299132c4528917941b35cf34e90&location="+district).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.d("a","成功获取数据");
                    Log.d("a","response.code()=="+response.code());
                    Log.d("a","response.body().string=="+response.code());
                }
            }
        });
    }
}