package com.yang.iwalker.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.BitmapTool;
import com.yang.iwalker.Location;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.R;
import com.yang.iwalker.Weather;
import com.yang.iwalker.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;
    Location location_info;

    private LocationClient mLocationClient;
    private LocationClientOption option;

    TextView location;
    TextView self_weather;
    DoOkHttp client;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1) {
        BlankFragment fragment = new BlankFragment();
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
    private View rootView;
    View getRootView() {
        return rootView;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        TextView contentTv = rootView.findViewById(R.id.content_tv);
        contentTv.setText(mContentText);

        //initLocation();
        location_info = new Location(getActivity().getApplicationContext());
        location_info.initLocation();
        initTextView();
        initEditText();
        new Thread(displayLocation).start();
        new Thread(displayWhether).start();
        client = new DoOkHttp();
        return rootView;
    }


    Handler mhandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    location.setText(location_info.city+location_info.district);
                    break;
                case 2:
                    //Toast.makeText(, "位置信息为空", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void initTextView(){
        location = rootView.findViewById(R.id.textView4);
        self_weather = rootView.findViewById(R.id.textView5);
    }
    //
    Runnable displayLocation = new Runnable() {
        @Override
        public void run() {
            while(location_info.city.equals("")){
            }
            Message msg = new Message();
            msg.what = 1;
            mhandler.sendMessage(msg);
        }
    };
    //获取天气线程
    Runnable displayWhether = new Runnable() {
        @Override
        public void run() {
            while(location_info == null || location_info.city.equals("")){
            }
            //showWhether();
            Weather w = new Weather(location_info.city);
            Message m = w.showWhether();
            whandler.sendMessage(m);
        }
    };
    Handler whandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle b = msg.getData();
                    self_weather.setText(b.getString("weather")+" : "+b.getString("tmp")+"℃");
                    break;
                case 2:
                    Toast.makeText(getContext(), "天气信息为空", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void initEditText(){
        //searchView = rootView.findViewById(R.id.ic_search);
    }
    RecyclerView recycle;
    HomeAdapter adatper;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycle = getActivity().findViewById(R.id.recycle_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycle.setLayoutManager(layoutManager);

        new Thread(infoRunnable).start();


    }
    JsonArray datas;
    List<Bitmap> list;
    Runnable infoRunnable = new Runnable() {
        @Override
        public void run() {
            datas = client.getAllActivity("20", "0");
            list = new ArrayList<>();
            for(int i =0;i < datas.size();i++){
                JsonObject o = datas.get(i).getAsJsonObject();
                if(!o.get("images").isJsonNull()){
                    JsonArray jArray = o.get("images").getAsJsonArray();
                    if(jArray.size()>0){
                        JsonObject j = jArray.get(0).getAsJsonObject();
                        Bitmap b = BitmapTool.returnBitMap(j.get("image").getAsString());
                        list.add(b);
                    }else{
                        list.add(null);
                    }
                } else{
                    list.add(null);
                }
            }
            Message m =new Message();
            m.what = 1;
            infoHandler.sendMessage(m);
        }
    };

    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adatper = new HomeAdapter(datas, getContext(), list, client);
                    recycle.setAdapter(adatper);
            }
            super.handleMessage(msg);
        }
    };


}

