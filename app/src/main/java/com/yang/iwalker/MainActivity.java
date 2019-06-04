package com.yang.iwalker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    Button b;
    private LocationClient mLocationClient;
    private LocationClientOption option;
    TextView location;
    TextView self_weather;
    Message msg;
    SearchView searchView;
    PoiSearch mPoiSearch;
    ListView lv1;
    MyAdapter adapter;

    String addr;
    String country;
    String province;
    String city = "";
    String district;
    String street;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initButton();
        initLocation();
        //initTextView();
        initMessege();
        initSearchView();
        initPOI();
        initListView();
    }

    private BDLocationListener mlistener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation == null){
                msg.what = 2;
                mhandler.handleMessage(msg);
            }
            addr = bdLocation.getAddrStr();    //获取详细地址信息
            country = bdLocation.getCountry();    //获取国家
            province = bdLocation.getProvince();    //获取省份
            city = bdLocation.getCity();    //获取城市
            district = bdLocation.getDistrict();    //获取区县
            street = bdLocation.getStreet();    //获取街道信息

            msg.what = 1;
            mhandler.sendMessage(msg);
        }
    };
    /*public void initTextView(){
        location = findViewById(R.id.location);
        self_weather = findViewById(R.id.weather);
    }*/

    Handler mhandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    location.setText("当前位置信息：\n"+province+"\n"+city+"\n"+district+"\n"+street+"\n");
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "位置信息为空", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void initMessege(){
        msg = new Message();
    }
    public void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mlistener);
        option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    public void initButton(){
        b = findViewById(R.id.map_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }



    public void initSearchView(){
        searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchPOI(s);
                return true;
            }
        });
    }

    public void initPOI(){
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
    }

    public void searchPOI(String s){
        if(s.equals("")){
            int sugdata_size = mData.size();
            if(sugdata_size>0){
                mData.removeAll(mData);
                lv1.setAdapter(adapter);
            }
        }else {
            mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city).keyword(s).pageCapacity(20));
        }
    }

    String[] datas_poi;
    String[] city_poi;
    String[] address_poi;
    LatLng[] position_poi;
    List<Map<String, Object>> mData;

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //Toast.makeText(MainActivity.this, "未搜索到POI数据", Toast.LENGTH_SHORT).show();
                lv1.setAdapter(null);
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                //获取POI检索结果
                //Toast.makeText(MainActivity.this, "已搜索到POI数据", Toast.LENGTH_SHORT).show();
                List<PoiInfo> allPoi = result.getAllPoi();
                int totalpages = result.getTotalPageNum();
                int n = allPoi.size();
                datas_poi = new String[n];
                city_poi = new String[n];
                address_poi = new String[n];
                position_poi = new LatLng[n];
                for (int i = 0; i < allPoi.size(); i++) {
                    datas_poi[i] = allPoi.get(i).name;   	  //获取的所有poi相关名字
                    city_poi[i] = allPoi.get(i).city;        //对应的城市
                    address_poi[i] = allPoi.get(i).address;  //对应的详细地址
                    position_poi[i]=allPoi.get(i).location; //对应的位置的经纬度
                }
                mData = getData();
                lv1.setAdapter(adapter);
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                String strInfo = "在";
                for (CityInfo cityInfo : result.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    public void initListView(){
        lv1 = findViewById(R.id.listview_location);
        adapter = new MyAdapter(MainActivity.this);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<datas_poi.length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("poi_name", datas_poi[i]);
            map.put("poi_city", city_poi[i]);
            map.put("poi_address", address_poi[i]);
            map.put("poi_latlng", position_poi[i]);
            list.add(map);
        }
        return list;
    }
    class MyAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            int size = mData.size();
            if (size>0){
                return mData.size()>=20 ? 20 : mData.size();
            }
            else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_item_location, null);
                convertView.setBackgroundColor(Color.WHITE);
                holder.poi_name = (TextView)convertView.findViewById(R.id.location_up_name);
                //holder.LinearLayout_poi=(LinearLayout) findViewById(R.id.layout_list_adapter);
                //holder.poi_info = (TextView)convertView.findViewById(R.id.location_down_name);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            //myApp.setpoiLatLng((LatLng)mData.get(position).get("poi_latlng"));
            holder.poi_name.setText((String)mData.get(position).get("poi_name")+"\n"+(String)mData.get(position).get("poi_address"));
            holder.poi_name.setTag(position);
            holder.poi_name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tag_poi", "true");
                    bundle.putString("poi_name", (String) mData.get(position).get("poi_name"));
                    bundle.putString("poi_city", (String) mData.get(position).get("poi_city"));
                    bundle.putString("poi_address", (String) mData.get(position).get("poi_address"));
                    bundle.putDouble("poi_lat", ((LatLng)mData.get(position).get("poi_latlng")).latitude);
                    bundle.putDouble("poi_lng", ((LatLng)mData.get(position).get("poi_latlng")).longitude);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            });

            //holder.LinearLayout_poi.setOnClickListener(MyListener(position));

            return convertView;
        }
    }

    class ViewHolder{
        TextView poi_name;
    }




}

