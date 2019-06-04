package com.yang.iwalker;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.yang.iwalker.NetWork.DoOkHttp;

public class FriendInfoActivity extends AppCompatActivity {
    String ID;
    DoOkHttp client;
    ImageView image;
    TextView nickname;
    TextView gender;
    TextView desc;
    Bitmap bitmap;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        /*沉浸式状态栏*/
        StatusBarUtils.translucent(this);
        Bundle b = getIntent().getExtras();
        ID = b.getString("ID");
        client = new DoOkHttp();
        View view = findViewById(R.id.text_del);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(FriendInfoActivity.this,"为什么要删除我？",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String s = client.deleteFriends(ID);
                        if(s.equals("0")){
                            Message m = new Message();
                            m.what = 2;
                            infoHandler.sendMessage(m);
                        }else{
                            Message m = new Message();
                            m.what = 3;
                            infoHandler.sendMessage(m);
                        }

                    }
                }).start();
            }
        });
        initView();
        new Thread(infoRunnable).start();
    }
    public void initView(){
        image = findViewById(R.id.img_head);
        nickname = findViewById(R.id.id);
        gender = findViewById(R.id.gender);
        desc = findViewById(R.id.text_sign);
    }

    Runnable infoRunnable = new Runnable() {
        @Override
        public void run() {
            JsonObject object = client.getUserFriends(ID);
            String username = object.get("userName").getAsString();
            String imageurl = "";
            String nickname = "";
            String gender = "";
            String desc = "";
            if(!object.get("image").isJsonNull()){
                imageurl = object.get("image").getAsString();
            }
            if(!object.get("nickname").isJsonNull()){
                nickname = object.get("nickname").getAsString();
            }
            if(!object.get("gender").isJsonNull()){
                if(object.get("gender").getAsString().equals("true"))
                    gender = "男";
                else
                    gender = "女";
            }
            if(!object.get("desc").isJsonNull()){
                desc = object.get("desc").getAsString();
            }
            Message msg = new Message();
            Bundle b = new Bundle();
            if(!imageurl.equals("")){
                bitmap = BitmapTool.returnBitMap(imageurl);
            }
            b.putString("imgurl", imageurl);
            b.putString("nickname", nickname);
            b.putString("username", username);
            b.putString("gender", gender);
            b.putString("desc", desc);
            msg.setData(b);
            msg.what = 1;
            infoHandler.sendMessage(msg);
        }
    };
    Handler infoHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle b = new Bundle();
                    b = msg.getData();
                    nickname.setText(b.getString("nickname")+"("+ID+")");
                    gender.setText(b.getString("gender"));
                    desc.setText(b.getString("desc"));
                    if(!b.getString("imgurl").equals("")){
                        image.setImageBitmap(bitmap);
                    }
                    break;
                case 2:
                    finish();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT);
            }
            super.handleMessage(msg);
        }
    };
}
