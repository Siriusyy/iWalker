package com.yang.iwalker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.adapter.NotifyAdapter;

import java.util.ArrayList;
import java.util.List;


public class NotifyActivity extends AppCompatActivity {
    DoOkHttp client;
    private RecyclerView recycle;
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_infor);
        client = new DoOkHttp();
        initInfo();
    }

    protected void initInfo(){
        recycle = findViewById(R.id.fri_info_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycle.setLayoutManager(layoutManager);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Thread(infoRunnable).start();
    }

    JsonArray datas;
    List<Bitmap> list;
    Runnable infoRunnable = new Runnable() {
        @Override
        public void run() {
            datas = client.showRequest();
            list = new ArrayList<>();
            if(datas.size()>0){

                for(int i =0;i < datas.size();i++){
                    JsonObject o = datas.get(i).getAsJsonObject();
                    if(!o.get("image").isJsonNull()){
                        Bitmap b = BitmapTool.returnBitMap(o.get("image").getAsString());
                        list.add(b);
                    }
                    else{
                        Bitmap b = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.logo);
                        list.add(b);
                    }
                }
                Message m = new Message();
                m.what = 1;
                mHandler.sendMessage(m);
            }
        }
    };
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    NotifyAdapter adatper = new NotifyAdapter(datas, list, client);
                    recycle.setAdapter(adatper);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
