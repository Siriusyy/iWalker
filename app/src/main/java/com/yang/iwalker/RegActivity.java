package com.yang.iwalker;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yang.iwalker.NetWork.DoOkHttp;

public class RegActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private EditText et_username, et_password1, et_password2;
    private EditText et_nickname;
    private RadioGroup rg_gender;
    private String userName,password1, password2, nickname;
    private boolean gender;
    private Button btn_reg;
    //private List<UserBean> list;
    private DoOkHttp client;

    private String regex = "^[a-z0-9A-Z]+$";

    private int USERNAME_LENGTH = 20;
    private int NICKNAME_LENGTH = 20;
    private int PASSWORD_LENGTH = 30;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        InitView();

    }
    public void InitView(){
        et_username = (EditText) findViewById(R.id.editText4);
        et_password1 = (EditText) findViewById(R.id.editText3);
        et_password2 = (EditText) findViewById(R.id.editText5);
        et_nickname = (EditText) findViewById(R.id.editText6);
        rg_gender = (RadioGroup) findViewById(R.id.radioGroup2);
        rg_gender.setOnCheckedChangeListener(this);
        btn_reg = (Button) findViewById(R.id.button4);
        client = new DoOkHttp();
        btn_reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Thread(connetRunnable).start();
            }
        });
    }

    Runnable connetRunnable = new Runnable() {
        @Override
        public void run() {
            regServer();
        }
    };

    public void regServer(){
        userName = et_username.getText().toString();
        password1 = et_password1.getText().toString();
        password2 = et_password2.getText().toString();
        nickname = et_nickname.getText().toString();

        if(!userName.matches(regex)  || TextUtils.isEmpty(userName) || userName.length() > USERNAME_LENGTH){
            Toast.makeText(this, "用户名错误,请检查用户名是否为空" +
                    "或含有字母数字外的其他字符，或用户名超出长度限制", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(TextUtils.isEmpty(nickname)|| nickname.length() > NICKNAME_LENGTH){
            Toast.makeText(this, "昵称错误，请检查昵称是否错误，或超过长度限制", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(!password1.equals(password2)){
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return ;
        }

        //处理返回的信息
        //gist为临时变量，前后端统一时修改。
        String status = client.regiest(userName, password1, nickname, gender);
        if(status.equals("0")){
            Message m = new Message();
            m.what = 0;
            infoHandler.sendMessage(m);
            Intent intent = new Intent(RegActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else{
            Message m = new Message();
            m.what = 1;
            infoHandler.sendMessage(m);
        }
    }
    Handler infoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    et_username.setText("");
                    et_password1.setText("");
                    et_password2.setText("");
                    et_nickname.setText("");
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void onCheckedChanged(RadioGroup group, int chenkedId){
        switch (chenkedId){
            case R.id.radioButton4:
                gender = true;
                break;
            case R.id.radioButton3:
                gender = false;
                break;
            default:
                break;

        }
    }


}

