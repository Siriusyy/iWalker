package com.yang.iwalker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xuexiang.xui.utils.StatusBarUtils;
import com.yang.iwalker.adapter.FriendsAdapter;
import com.yang.iwalker.adapter.NotifyAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotifyActivity extends AppCompatActivity {

    private RecyclerView recycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_infor);

        initInfo();
    }

    protected void initInfo(){
        recycle = findViewById(R.id.fri_info_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycle.setLayoutManager(layoutManager);
        List<Map<String, Object>> datas=new ArrayList<>();
        for(int i=0;i<20;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("friendName", i);
            map.put("friendName", "蔡徐坤");
            map.put("info", "唱跳rap篮球");
            //map.put("image", "5月21日");

            datas.add(map);
        }
        NotifyAdapter adatper = new NotifyAdapter(datas);
        recycle.setAdapter(adatper);
    }

}
