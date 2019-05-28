package com.yang.iwalker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;

public class MapActivity extends AppCompatActivity {

    private MapView mMap = null;	//百度地图控件,专门显示地图用的控件
    private BaiduMap bdMap;			//百度地图对象,抽象的地图对象
    private LocationMode currentMode;	//定位模式
    public LocationClient locClient = null;
    private boolean isFirstLoc = true;	//记录是否第一次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        initMap();										//初始化百度地图
        initButton();                           //初始化按钮
    }
    private void initMap() {

        mMap = (MapView) findViewById(R.id.map_view);
        //bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);	//定义地图状态
        mMap.showScaleControl(false); 						//隐藏比例尺
        mMap.showZoomControls(false); 						//隐藏缩放控件																							1
        View child = this.mMap.getChildAt(1);
        if(child != null && (child instanceof ImageView)) {
            child.setVisibility(View.INVISIBLE);
        }													//隐藏baidu-logo
        bdMap = mMap.getMap();								//获得地图对象
        bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);		    //普通地图
        bdMap.setMapStatus(msu); 							//设置地图初始状态
        currentMode = LocationMode.FOLLOWING;				//当前定位模式为：普通
        bdMap.setMyLocationEnabled(true);					//开启定位图层
        bdMap.setTrafficEnabled(true); 					//开启交通图
        bdMap.setMyLocationConfiguration(new MyLocationConfiguration(currentMode, true, null));
        locClient = new LocationClient(getApplicationContext());	        //定位服务的客户端
        //BDLocationListener listener = new MyLocationListener();
        locClient.registerLocationListener(listener);	//注册监听函数
        initoption();
        locClient.start();					//启动定位
    }
    private void initoption() {
        LocationClientOption option = new LocationClientOption();	//配置LocationClient这个定位客户端的定位参数
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式，高精度，低功耗，仅设备
        option.setOpenGps(true);			//打开gps
        option.setCoorType("bd09ll");		//设置坐标类型
        option.setIsNeedAddress(true);		//设置是否需要地址信息，默认不需要
        option.setScanSpan(1300);				//1s后定位
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.setNeedDeviceDirect(true); 	//返回的定位结果包含手机机头方向
        locClient.setLocOption(option);		//配置客户端

    }

    private BDLocationListener listener = new BDLocationListener(){
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(mMap == null || location == null)
                return;
            if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(MapActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(MapActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(MapActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                return;
            }

            if(isFirstLoc){
                LatLng l1 = new LatLng(location.getLatitude(), location.getAltitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(l1).zoom(14.0f);
                bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                isFirstLoc = false;
            }
            /*else{
                MyLocationData locationData = new MyLocationData.Builder()
                        .accuracy(0)
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                bdMap.setMyLocationData(locationData);
                LatLng  ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //builder.target(ll).zoom(14.0f);
                bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }*/
        }
    };

    public void initButton(){
        Button search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //myOrientationListener.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //myOrientationListener.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locClient.stop();
        mMap.onDestroy();
        bdMap.setMyLocationEnabled(false);
    }

}
