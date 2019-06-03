package com.yang.iwalker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ListView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.SuggestionSearch;

public class MapActivity extends AppCompatActivity {

    private MapView mMap = null;	//百度地图控件,专门显示地图用的控件
    public LocationClient locClient = null;
    private boolean loc_myself = true;
    private BaiduMap bdMap;

    int signal = 0;
    LocationInfo locinfo;
    private SearchView searchView;
    private ListView searchList2;
    private SuggestionSearch suggestionSearch;
    String city;
    LatLng self_position;
    Marker marker;
    //SugAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                signal = 1;
                locinfo = new LocationInfo();
                locinfo.setName(bundle.getString("poi_name"));
                locinfo.setCity(bundle.getString("poi_city"));
                locinfo.setAddress(bundle.getString("poi_address"));
                locinfo.setPosition(new LatLng(bundle.getDouble("poi_lat"), bundle.getDouble("poi_lng")));
                loc_myself = false;
            }

        }
        //initSearchView();
        //initSuggestSearch();
        //initListView();
    }


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
            bdMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
        }
    }
    /*public void initListView(){
        searchList2 = findViewById(R.id.search_list2);
        adapter = new SugAdapter(MapActivity.this);
    }*/
    /*public void initSuggestSearch(){
        suggestionSearch = SuggestionSearch.newInstance();
        suggestionSearch.setOnGetSuggestionResultListener(msuggerstListener);
    }*/
    /*String[] key_sug;
    String[] city_sug;
    String[] district_sug;
    LatLng[] position_sug;
    String[] distance_sug;
    List<Map<String, Object>> sugData;*/
    /*private OnGetSuggestionResultListener msuggerstListener = new OnGetSuggestionResultListener() {
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
            searchList2.setAdapter(adapter);
        }
    };*/
    /*private List<Map<String, Object>> getData() {
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
    }*/
    /*class SugAdapter extends BaseAdapter {
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
            //myApp.setpoiLatLng((LatLng)mData.get(position).get("poi_latlng"));
            holder.sugInfo.setText((String)sugData.get(position).get("key_sug")+"\n"+(String)sugData.get(position).get("city_sug")+(String)sugData.get(position).get("district_sug"));
            holder.sugDistance.setText(sugData.get(position).get("distance_sug")+"m");
            holder.sugInfo.setTag(position);
            holder.sugDistance.setTag(position);
            holder.sugInfo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loc_myself = false;
                    setPersonMarker((LatLng) sugData.get(position).get("position_sug"));
                    searchView.clearFocus();
                    int sugdata_size = sugData.size();
                    if(sugdata_size>0){
                        sugData.removeAll(sugData);
                        searchList2.setAdapter(adapter);
                    }
                }

            });

            //holder.LinearLayout_poi.setOnClickListener(MyListener(position));

            return convertView;
        }
    }

    class ViewHolderSug{
        TextView sugInfo;
        TextView sugDistance;
    }*/

    /*public void searchSuggestView(String s){
        if(s.equals("")){
            //sugData.clear();
            int sugdata_size = sugData.size();
            if(sugdata_size>0){
                sugData.removeAll(sugData);
                searchList2.setAdapter(adapter);
            }
        }
        else{
            suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(city).keyword(s));
        }

    }*/
    /*public void initSearchView(){
        searchView = findViewById(R.id.search_view2);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchSuggestView(s);
                return true;
            }
        });
    }*/

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
