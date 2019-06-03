package com.yang.iwalker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;
import com.yang.iwalker.R;
import com.yang.iwalker.dialog.BottomDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;

    private boolean isFirstLoc;	//记录是否第一次定位
    private boolean loc_myself;
    private boolean firstMarkerPos;
    private BaiduMap bdMap;			//百度地图对象,抽象的地图对象
    private MyLocationConfiguration.LocationMode currentMode;	//定位模式
    private MapView mMap = null;	//百度地图控件,专门显示地图用的控件
    private SuggestionSearch suggestionSearch;  //热词搜索
    private MaterialSearchView mSearchView;     //搜索框
    private ListView sug_listview;
    BottomDialog bottomDialog;
    View v;                     //Bottomdialog

    public LocationClient locClient = null;
    String city;
    LatLng self_position;
    int signal = 0;
    Marker marker;
    SugAdapter adapter;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static MapFragment newInstance(String param1) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchView = getActivity().findViewById(R.id.navi_search);
        getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        mSearchView.showSearch();
                                                                    }
                                                                });
        initSearchViews();                              //初始化搜索框
        initMap();										//初始化百度地图
        initSuggestSearch();                            //初始化热词搜索
        initListView();
        initBottomDialog();                             //初始化底部组件
        initValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        TextView contentTv = rootView.findViewById(R.id.content_tv);
        contentTv.setText(mContentText);


        return rootView;
    }


    //初始化百度地图
    private void initMap() {
        mMap = (MapView)getActivity().findViewById(R.id.view_map);
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
        currentMode = MyLocationConfiguration.LocationMode.FOLLOWING;				//当前定位模式为：普通
        bdMap.setMyLocationEnabled(true);					//开启定位图层
        bdMap.setTrafficEnabled(false); 					//开启交通图
        bdMap.setMyLocationConfiguration(new MyLocationConfiguration(currentMode, true, null));
        bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                bottomDialog.show();
                return true;
            }
        });
        locClient = new LocationClient(getActivity().getApplicationContext());	        //定位服务的客户端
        //BDLocationListener listener = new MyLocationListener();
        locClient.registerLocationListener(listener);	//注册监听函数
        initoption();
        locClient.start();					//启动定位
    }
    //初始化百度地图配置选项
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
    //定位监听
    private BDLocationListener listener = new BDLocationListener(){
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(mMap == null || location == null)
                return;
            if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(getContext(), "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(getContext(), "网络错误，请检查", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(getContext(), "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                return;
            }

            city = location.getCity();
            self_position = new LatLng(location.getLatitude(), location.getLongitude());

            if(loc_myself && isFirstLoc){
                LatLng l1 = new LatLng(location.getLatitude(), location.getAltitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(l1).zoom(14.0f);
                bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                isFirstLoc = false;

            }
            else if(loc_myself && signal == 0){
                MyLocationData locationData = new MyLocationData.Builder()
                        .accuracy(0)
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                bdMap.setMyLocationData(locationData);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //builder.target(ll).zoom(14.0f);
                bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            else if(signal == 1 && firstMarkerPos){
                //setPersonMarker(locinfo.getPosition());
                firstMarkerPos = false;
            }
        }
    };

    String[] key_sug;
    String[] city_sug;
    String[] district_sug;
    LatLng[] position_sug;
    String[] distance_sug;
    List<Map<String, Object>> sugData;

    //初始化热词搜索
    public void initSuggestSearch(){
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if(suggestionResult == null || suggestionResult.getAllSuggestions() == null){
                    return;
                }
                List<SuggestionResult.SuggestionInfo> sInfo= suggestionResult.getAllSuggestions();

                int n = sInfo.size();
                key_sug = new String[n];
                city_sug = new String[n];
                district_sug = new String[n];
                position_sug = new LatLng[n];
                distance_sug = new String[n];

                for(int i = 0;i<n;i++){
                    key_sug[i] = sInfo.get(i).key;
                    city_sug[i] = sInfo.get(i).city;
                    district_sug[i] = sInfo.get(i).district;
                    position_sug[i] = sInfo.get(i).pt;
                    DecimalFormat df = new DecimalFormat("######0");
                    distance_sug[i] = df.format(DistanceUtil.getDistance(self_position, sInfo.get(i).pt));
                }
                sugData = getData();
                sug_listview.setAdapter(adapter);
            }
        });
    }
    //适配器
    class SugAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public SugAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            int size = sugData.size();
            if (size>0){
                return sugData.size()>=20 ? 20 : sugData.size();
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
            ViewHolderSug holder = null;
            if (convertView == null) {
                holder = new ViewHolderSug();
                convertView = mInflater.inflate(R.layout.listview_item_sugdistance, null);
                convertView.setBackgroundColor(Color.WHITE);
                holder.sugInfo = (TextView)convertView.findViewById(R.id.locationInfo);
                holder.sugDistance = (TextView)convertView.findViewById(R.id.distanceInfo);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolderSug) convertView.getTag();
            }
            holder.sugInfo.setText((String)sugData.get(position).get("key_sug")+"\n"+(String)sugData.get(position).get("city_sug")+(String)sugData.get(position).get("district_sug"));
            holder.sugDistance.setText(sugData.get(position).get("distance_sug")+"m");
            holder.sugInfo.setTag(position);
            holder.sugDistance.setTag(position);
            holder.sugInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loc_myself = false;
                    setPersonMarker((LatLng) sugData.get(position).get("position_sug"));

                    Bundle bundle = new Bundle();
                    bundle.putString("key_sug", (String) sugData.get(position).get("key_sug"));
                    bundle.putString("loc_sug", (String) sugData.get(position).get("city_sug")+(String)sugData.get(position).get("district_sug"));
                    transate(bundle);

                    bottomDialog.show();
                    mSearchView.clearFocus();
                    int sugdata_size = sugData.size();
                    if(sugdata_size>0){
                        sugData.removeAll(sugData);
                        sug_listview.setAdapter(adapter);
                    }
                }

            });
            return convertView;
        }
    }

    class ViewHolderSug{
        TextView sugInfo;
        TextView sugDistance;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<city_sug.length;i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key_sug", key_sug[i]);
            map.put("city_sug", city_sug[i]);
            map.put("district_sug", district_sug[i]);
            map.put("position_sug", position_sug[i]);
            map.put("distance_sug", distance_sug[i]);
            list.add(map);
        }
        return list;
    }

    //设置Marker
    private void setPersonMarker(LatLng personPoint) {
        if (null != personPoint) {
            if(marker!=null){
                marker.remove();
            }
            bdMap.setMyLocationEnabled(true);
            /*MyLocationData locData = new MyLocationData.Builder()
                    .latitude(personPoint.latitude)
                    .longitude(personPoint.longitude).build();
            bdMap.setMyLocationData(locData);*/

            LatLng ll = new LatLng(personPoint.latitude,
                    personPoint.longitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            bdMap.animateMapStatus(u);
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_loc);
            OverlayOptions option = new MarkerOptions().position(ll).icon(bitmap);
            marker = (Marker)bdMap.addOverlay(option);
            bdMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(18).build()));
        }
    }
    //初始化值
    public void initValue(){
        isFirstLoc = true;	//记录是否第一次定位
        loc_myself = true;
        firstMarkerPos = true;
    }
    //传递bundle
    public void transate(Bundle bundle){
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = 1;
        t_handler.sendMessage(msg);
    }
    //向dialog传值需要handler
    Handler t_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ((TextView)v.findViewById(R.id.sug_name)).setText(msg.getData().getString("key_sug"));
                ((TextView)v.findViewById(R.id.sug_loc)).setText(msg.getData().getString("loc_sug"));
            }
        }
    };
    //初始化底部组件
    public void initBottomDialog(){
        bottomDialog = new BottomDialog(getContext());
        v=View.inflate(getContext(),R.layout.dialog_bottom,null);
        bottomDialog.setContentView(v,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    //初始化列表框
    public void initListView(){
        sug_listview = getActivity().findViewById(R.id.sug_listview);
        adapter = new SugAdapter(getContext());
    }
    //初始化搜索栏
    protected void initSearchViews() {
        mSearchView.setVoiceSearch(true);
        mSearchView.setEllipsize(true);
        //mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SnackbarUtils.Long(mSearchView, "Query: " + query).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索栏文字改变
                if(newText.equals("")){
                    if(sugData!=null){
                        int sugdata_size = sugData.size();
                        if(sugdata_size>0){
                            sugData.removeAll(sugData);
                            sug_listview.setAdapter(adapter);
                        }
                    }
                }
                else{
                    suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(newText));
                }
                return true;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //搜索栏打开
            }

            @Override
            public void onSearchViewClosed() {
                //搜索栏关闭
                if(sugData!=null){
                    int sugdata_size = sugData.size();
                    if(sugdata_size>0){
                        sugData.removeAll(sugData);
                        sug_listview.setAdapter(adapter);
                    }
                }
            }
        });
        mSearchView.setSubmitOnClick(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
        initValue();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        super.onDestroyView();
    }

}
