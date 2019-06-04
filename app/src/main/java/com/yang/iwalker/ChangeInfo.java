package com.yang.iwalker;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yang.iwalker.NetWork.DoOkHttp;

public class ChangeInfo extends AppCompatActivity {
    EditText user_nickname_now;
    EditText user_password_now;
    EditText user_repassword_now;
    EditText user_desc_now;
    TextView back;
    Button publish;
    RadioGroup user_gender_now;
    DoOkHttp client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_infor_change);
        client = new DoOkHttp();
        initView();
        initButton();
        new Thread(initRunnable).start();
    }
    public void initView(){
        user_nickname_now = findViewById(R.id.user_nickname_now);
        user_password_now = findViewById(R.id.user_password_now);
        user_repassword_now = findViewById(R.id.user_repassword_now);
        user_desc_now = findViewById(R.id.user_desc_now);
        back = findViewById(R.id.back);
        publish = findViewById(R.id.publish);
        user_gender_now = findViewById(R.id.user_gender_now);
    }
    public void initButton(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        publishInfo();
                        Message m = new Message();
                        m.what = 1;
                        infoHandler.sendMessage(m);
                    }
                }).start();
            }
        });
    }

    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Bundle b = msg.getData();
                    user_nickname_now.setText(b.getString("nickname"));
                    if(b.getString("gender").equals("男")){
                        RadioButton rb = (RadioButton)(user_gender_now.getChildAt(1));
                        rb.setChecked(true);
                    }else{
                        RadioButton rb = (RadioButton)(user_gender_now.getChildAt(0));
                        rb.setChecked(true);
                    }
                    user_desc_now.setText(b.getString("signature"));
            }
            super.handleMessage(msg);
        }
    };

    Runnable initRunnable = new Runnable() {
        @Override
        public void run() {
            JsonObject user = client.getUserInfo();
            Message m = new Message();
            Bundle b = new Bundle();
            if(!user.get("nickname").toString().equals("null"))
                b.putString("nickname", user.get("nickname").getAsString());
            else{
                b.putString("nickname", "");
            }

            if(user.get("gender").getAsString().equals("true")){
                b.putString("gender", "男");
            }else{
                b.putString("gender", "女");
            }

            if(!user.get("desc").toString().equals("null"))
                b.putString("signature",user.get("desc").getAsString());
            else{
                b.putString("signature","");
            }
            m.setData(b);
            m.what = 2;
            infoHandler.sendMessage(m);
        }
    };

    public void publishInfo(){
        if(!user_nickname_now.getText().toString().equals("")){
            client.modifyNickname(user_nickname_now.getText().toString());
        }
        for(int i =0; i<user_gender_now.getChildCount();i++){
            RadioButton rb = (RadioButton)user_gender_now.getChildAt(i);
            if(rb.isChecked()){
                if(i==0){
                    client.modifyGender("false");
                }else{
                    client.modifyGender("true");
                }
                break;
            }
        }
        if(!user_password_now.getText().toString().equals("")){
            if(user_password_now.getText().toString().equals(user_repassword_now.getText().toString())){

            }
        }
        if(!user_desc_now.getText().toString().equals("")){
            client.modifyDesc(user_desc_now.getText().toString());
        }
    }
}
