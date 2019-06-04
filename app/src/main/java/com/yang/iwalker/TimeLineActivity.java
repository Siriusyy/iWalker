package com.yang.iwalker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;
import com.yang.iwalker.layout.TimelineLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeLineActivity extends AppCompatActivity{

    private TimelineLayout mTimelineLayout;
    DoOkHttp client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        client = new DoOkHttp();
        initView();
        new Thread(infoRunnable).start();
    }

    private void initView() {
        mTimelineLayout = (TimelineLayout) findViewById(R.id.timeline_layout);
    }

    private int index = 0;
    //添加
    private void addItem(JsonObject item) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_timeline, mTimelineLayout, false);
        String time = stamp2Date(item.get("createTime").getAsString());
        ((TextView) view.findViewById(R.id.tv_action_time)).setText(time);

        String li = item.get("locationName").getAsString();
        String[] list = li.split(",");
        String s = list[2] +" "+ list[3].substring(0, list[3].length()-1);
        ((TextView) view.findViewById(R.id.tv_action_status)).setText(s);

        mTimelineLayout.addView(view);
        index++;
    }
    //删除
    private void subItem() {
        if (mTimelineLayout.getChildCount() > 0) {
            mTimelineLayout.removeViews(mTimelineLayout.getChildCount() - 1, 1);
            index--;
        }
    }
    JsonArray datas;
    Runnable infoRunnable = new Runnable() {

        @Override
        public void run() {
            datas = client.getSelfActivity("20", "0");
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    for(int i=0; i<datas.size();i++){
                        addItem(datas.get(i).getAsJsonObject());
                    }
            }
            super.handleMessage(msg);
        }
    };
    public static String stamp2Date(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
