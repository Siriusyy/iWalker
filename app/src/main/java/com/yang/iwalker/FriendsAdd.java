package com.yang.iwalker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.adapter.AddFriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdd extends AppCompatActivity {
    EditText mSearch;
    TextView mButton;
    RecyclerView msearchList;
    DoOkHttp client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friend);
        client = new DoOkHttp();
        initView();
    }
    public void initView(){
        mSearch = findViewById(R.id.publish_title);
        mButton = findViewById(R.id.search_btn);
        msearchList = findViewById(R.id.recycle_find_friends);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(infoRunnable).start();
            }
        });
    }
    JsonArray datas;
    List<Bitmap> bitmapList;
    Runnable infoRunnable = new Runnable() {
        @Override
        public void run() {
            //datas =
            bitmapList = new ArrayList<>();
            String s = mSearch.getText().toString();
            if(!s.equals("")) {
                datas = client.findFriend(s);
                for (int i = 0; i < datas.size(); i++) {
                    JsonObject object = datas.get(i).getAsJsonObject();
                    Bitmap bitmap = null;
                    if(!object.get("image").isJsonNull()){
                        String si = object.get("image").getAsString();
                        bitmap = BitmapTool.returnBitMap(si);

                    }
                    bitmapList.add(bitmap);
                }

                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }

        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(datas!=null || bitmapList!=null){
                        AddFriendAdapter adatper=new AddFriendAdapter(datas, bitmapList, client, getApplicationContext());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        msearchList.setLayoutManager(layoutManager);
                        msearchList.setAdapter(adatper);
                    }else{
                        Toast.makeText(getApplicationContext(), "未搜索到", Toast.LENGTH_SHORT);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
