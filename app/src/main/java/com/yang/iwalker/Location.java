package com.yang.iwalker;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Location {

    private LocationClient mLocationClient;
    private LocationClientOption option;
    Context context;

    public String addr;
    public String country;
    public String province;
    public String city = "";
    public String district;
    public String street;
    public double latitude;
    public double longitude;
    public Location(Context context){
        this.context = context;
    }

    public void initLocation(){
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(mlistener);
        option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //位置监听
    private BDLocationListener mlistener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            addr = bdLocation.getAddrStr();    //获取详细地址信息
            country = bdLocation.getCountry();    //获取国家
            province = bdLocation.getProvince();    //获取省份
            city = bdLocation.getCity();    //获取城市
            district = bdLocation.getDistrict();    //获取区县
            street = bdLocation.getStreet();    //获取街道信息
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();
        }
    };
}
